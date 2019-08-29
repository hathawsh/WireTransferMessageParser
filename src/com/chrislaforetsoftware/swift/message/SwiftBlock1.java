/* SwiftBlock1.java
 *
 * Copyright (c) 2010, Chris Laforet Software/Christopher Laforet
 * All Rights Reserved
 *
 * Started: Nov 28, 2010
 * Revision Information: $Date: 2010-12-01 12:11:55 $
 *                       $Revision: 1.1 $
 * 
 * The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * The Initial Developer of the Original Code is Chris Laforet from Chris Laforet Software.
 * Portions created by Chris Laforet Software are Copyright (C) 2010.  All Rights Reserved.
 *
 * Contributor(s): Chris Laforet Software.
 */
 
package com.chrislaforetsoftware.swift.message;
 
/** Contains a Swift message block 1.
 * 
 * @author Christopher Laforet
 */
 
public class SwiftBlock1 implements ISwiftBlock
    {
    private char _appID;
    private String _serviceID;
    private String _bic;
    private char _lt;
    private String _branch;
    private String _sessionNo;
    private String _sequence;
 
    /** Package private constructor that extracts the 
     * block 1 items from the contents (everything between
     * the {1: and the close } non-inclusive) 
     * 
     * @param Contents
     */
    SwiftBlock1(String Contents)
        {
        _appID = Contents.charAt(0);
        _serviceID = Contents.substring(1,3);
        _bic = Contents.substring(3,11);
        _lt = Contents.charAt(11);
        _branch = Contents.substring(12,15);
        _sessionNo = Contents.substring(15,19);
        _sequence = Contents.substring(19,25);
        }
 
 
    /** Returns the Application Identifier. F is FIN, L is GPA Service message, 
     * and A is GPA system or service message.
     */
    public char getAppID()
        {
        return _appID;
        }
 
 
    /** Returns the service identifier which consists of 2 numeric chars. This identifies the type of data being sent or received. 01 is for all GPA, FIN, and user-to-user messages. 21 is message ACK/NACK/UAK/UNK. 03 is a select command.
     * 
     * @return the service ID.
     */
    public String getServiceID()
        {
        return _serviceID;
        }
 
 
    /** Returns the 8-character BIC (Bank Identification Code). If the message is an O 
     * message, this is the receiver or if it is an I message, this is the sender.
     * 
     * @return the BIC8 code.
     */
    public String getBIC8()
        {
        return _bic;
        }
 
 
    /** Returns the 11-character BIC (Bank Identification Code) which comprises 
     * the 8-character code + Branch code. If the message is an O message, this 
     * is the receiver or if it is an I message, this is the sender.
     * 
     * @return The BIC11 code.
     */
    public String getBIC11()
        {
        return _bic + _branch;
        }
 
 
    /** Returns the 12-character BIC (Bank Identification Code) which comprises 
     * the 8-character code + LT + Branch code. If the message is an O message, 
     * this is the receiver or if it is an I message, this is the sender.
     * 
     * @return The BIC12 code.
     */
    public String getBIC12()
        {
        return _bic + _lt + _branch;
        }
 
 
    /** Returns the LT (Logical Terminal) of the BIC. If the message 
     * is an O message, this is the receiver or if it is an I message, 
     * this is the sender.
     * 
     * @return The LT.
     */
    public char getLT()
        {
        return _lt;
        }
 
     
    /** Returns the 3-character branch code of the BIC. If the message is an O message, this is the receiver or if it is an I message, this is the sender.
     * 
     * @return the Branch code.
     */
    public String getBranch()
        {
        return _branch;
        }
 
 
    /** Returns the 4-digit session number in which this message was transmitted.
     * 
     * @return the session number
     */
    public String getSessionNo()
        {
        return _sessionNo;
        }
 
 
    /** Returns the ISN or OSN of this message and consists of 6 digits. If the 
     * message is an O message, it is the OSN otherwise it is the ISN for an I message.
     * 
     * @return the ISN/OSN sequence.
     */
    public String getSequence()
        {
        return _sequence;
        }
 
 
    /** Returns a properly formatted block 1 including braces and 1: identifier.
     * 
     * @return a complete block 1.
     */
    @Override
    public String toString()
        {
        return getFormattedBlock();
        }
 
     
    /* (non-Javadoc)
     * @see com.chrislaforetsoftware.swift.message.ISwiftBlock#getContents()
     */
    public String getContents()
        {
        StringBuffer sb = new StringBuffer(32);
        sb.append(_appID);
        sb.append(_serviceID);
        sb.append(getBIC12());
        sb.append(_sessionNo);
        sb.append(_sequence);
        return sb.toString();
        }
 
 
    /* (non-Javadoc)
     * @see com.chrislaforetsoftware.swift.message.ISwiftBlock#getFormattedBlock()
     */
    public String getFormattedBlock()
        {
        StringBuffer sb = new StringBuffer(32);
        sb.append("{1:");
        sb.append(getContents());
        sb.append("}");
        return sb.toString();
        }
    }