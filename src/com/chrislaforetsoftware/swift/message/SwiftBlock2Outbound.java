/* SwiftBlock2Outbound.java
 *
 * Copyright (c) 2010, Chris Laforet Software/Christopher Laforet
 * All Rights Reserved
 *
 * Started: Nov 29, 2010
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
 
/** Contains a Swift Block 2 on Output from Swift.
 * 
 * @author Christopher Laforet
 */
 
public class SwiftBlock2Outbound implements ISwiftBlock
    {
    private String _mt;
    private String _inputTime;
    private String _inputDate;
    private String _bic;
    private char _lt;
    private String _branch;
    private String _sessionNumber;
    private String _sequence;
    private String _outputDate;
    private String _outputTime;
    private char _priority;
 
    /// <summary>
    /// Creates a SwiftBlock 2 O entry with the contents of the 2: block.  Contents
    /// refers to the values omitting the braces and the 2:.
    /// </summary>
    /// <param name="Block2Contents"></param>
    SwiftBlock2Outbound(String Contents)
        {
        while (Contents.length() < 47)
            Contents = Contents + "X";
 
        _mt = Contents.substring(1,4);
        _inputTime = Contents.substring(4,8);
        _inputDate = Contents.substring(8,14);
        _bic = Contents.substring(14,22);
        _lt = Contents.charAt(22);
        _branch = Contents.substring(23,26);
        _sessionNumber = Contents.substring(26,30);
        _sequence = Contents.substring(30,36);
        _outputDate = Contents.substring(36,42);
        _outputTime = Contents.substring(42,46);
        _priority = Contents.charAt(46);
        }
 
 
    /** Returns the MT (Message Type) code which is a 3-digit, zero- padded value.
     * 
     * @return the MT code padded with preceding zeroes.
     */
    public String getMT()
        {
        return _mt;
        }
 
 
    /** Return the sender time formatted as HHMM (it is a time local to the sender
     * unless this is a system message in which case it is UTC).
     * 
     * @return a time string formatted as HHMM
     */
    public String getInputTime()
        {
        return _inputTime;
        }
     
     
    /** Returns the YYMMDD sender local date for this message unless this is a system 
     * message in which case it is UTC.
     * 
     * @return a YYMMDD formatted string.
     */
    public String getInputDate()
        {
        return _inputDate;
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
     * the 8-character code + Branch code. If the message is an O message, 
     * this is the receiver or if it is an I message, this is the sender.
     * 
     * @return The BIC11 code.
     */
    public String getBIC11()
        {
        return _bic + _branch;
        }
 
 
    /** Return the 12-character BIC (Bank Identification Code) which comprises 
     * the 8-character code + LT + Branch code. If the message is an O message, 
     * this is the receiver or if it is an I message, this is the sender.
     * 
     * @return The BIC12 code.
     */
    public String getBIC12()
        {
        return _bic + _lt + _branch;
        }
 
 
    /** Return the LT (Logical Terminal) of the BIC. If the message is an O message, 
     * this is the receiver or if it is an I message, this is the sender.
     * 
     * @return The LT.
     */
    public char getLT()
        {
        return _lt;
        }
 
 
    /** Returns the 3-character branch code of the BIC. If the message is an O 
     * message, this is the receiver or if it is an I message, this is the sender.
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
        return _sessionNumber;
        }
 
 
    /** Returns the ISN or OSN of this message and consists of 6 digits. 
     * If the message is an O message, it is the OSN otherwise it is the 
     * ISN for an I message.
     * 
     * @return the ISN/OSN sequence.
     */
    public String getSequence()
        {
        return _sequence;
        }
 
 
    /** Retrieves the HHMM receiver time (local time for this message unless this is a system
     * message in which case it is UTC).
     * 
     * @return a time string formatted as HHMM
     */
    public String getOutputTime()
        {
        return _outputTime;
        }
 
 
    /** Retrieves the YYMMDD receiver local date for this message unless this is a system 
     * message in which case it is UTC.
     * 
     * @return a YYMMDD formatted string.
     */
    public String getOutputDate()
        {
        return _outputDate;
        }
 
     
    /** Returns the priority of the FIN message. S is system, U is urgent, and N is normal.
     * 
     * @return the priority of the message.
     */
    public char getPriority()
        {
        return _priority;
        }
 
 
    /** Returns a properly formatted block 2 including braces and 2: identifier.
     * 
     * @return a complete block 2.
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
        StringBuffer sb = new StringBuffer(64);
        sb.append("O");
        sb.append(_mt);
        sb.append(_inputTime);
        sb.append(_inputDate);
        sb.append(getBIC12());
        sb.append(_sessionNumber);
        sb.append(_sequence);
        sb.append(_outputDate);
        sb.append(_outputTime);
        sb.append(_priority);
        return sb.toString();
        }
 
 
    /* (non-Javadoc)
     * @see com.chrislaforetsoftware.swift.message.ISwiftBlock#getFormattedBlock()
     */
    public String getFormattedBlock()
        {
        StringBuffer sb = new StringBuffer(64);
        sb.append("{2:");
        sb.append(getContents());
        sb.append("}");
        return sb.toString();
        }
    }
