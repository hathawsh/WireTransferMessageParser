/* SwiftBlock2Inbound.java
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
 
/** Contains a Swift Block 2 on Input to Swift.
 * 
 * @author Christopher Laforet
 */
 
public class SwiftBlock2Inbound implements ISwiftBlock
    {
    private String _mt;
    private String _bic;
    private char _lt;
    private String _branch;
    private char _priority;
    private boolean _hasMonitoring = false;
    private char _monitoring = '2';
    private boolean _hasObsolescence = false;
    private String _obsolescence;
 
    /** Package private constructor that creates a SwiftBlock 2 I entry with the 
     * contents of the 2: block. Contents refers to the values omitting the braces and 
     * the 2:. The SwiftBlock2 superclass knows how to handle this.
     * 
     * @param Contents the contents in the 2 block.
     */
    SwiftBlock2Inbound(String Contents)
        {
        while (Contents.length() < 18)
            Contents = Contents + "X";
 
        _mt = Contents.substring(1,4);
        _bic = Contents.substring(4,12);
        _lt = Contents.charAt(12);
        _branch = Contents.substring(13,16);
        _priority = Contents.charAt(16);
 
        if (Contents.length() > 18)
            {
            _monitoring = Contents.charAt(17);
            _hasMonitoring = true;
 
            if (Contents.length() >= 21)
                {
                _obsolescence = Contents.substring(18,21);
                _hasObsolescence = true;
                }
            else
                _hasObsolescence = false;
            }
        else
            {
            _hasMonitoring = false;
            _hasObsolescence = false;
            }
        }
 
 
    /** Returns the MT (Message Type) code which is a 3-digit, zero padded value.
     * 
     * @return the MT code padded with preceding zeroes.
     */
    public String getMT()
        {
        return _mt;
        }
 
 
    /** Returns the 8-character Bank Identification Code. If the message is an O message, this is the receiver or if it is an I message, this is the sender.
     * 
     * @return the BIC8 code.
     */
    public String getBIC8()
        {
        return _bic;
        }
 
 
    /** Returns the 11-character Bank Identification Code which comprises the 8-character code + Branch code. If the message is an O message, this is the receiver or if it is an I message, this is the sender.
     * 
     * @return The BIC11 code.
     */
    public String getBIC11()
        {
        return _bic + _branch;
        }
 
 
    /** Returns the 12-character Bank Identification Code which comprises the 8-character code + LT + Branch code. If the message is an O message, this is the receiver or if it is an I message, this is the sender.
     * 
     * @return The BIC12 code.
     */
    public String getBIC12()
        {
        return _bic + _lt + _branch;
        }
 
 
    /** Returns the Logical Terminal of the BIC. If the message is an O message, this is the receiver or if it is an I message, this is the sender.
     * 
     * @return The LT.
     */
    public char getLT()
        {
        return _lt;
        }
 
 
    /** Returns the 3-character branch code of the BIC. If the message is an 
     * O message, this is the receiver BIC or if it is an I message, this 
     * is the sender BIC.
     * 
     * @return the Branch code.
     */
    public String getBranch()
        {
        return _branch;
        }
 
 
    /** Returns the priority of the FIN message. S is system, U is urgent, and N is normal.
     * 
     * @return the priority of the message.
     */
    public char getPriority()
        {
        return _priority;
        }
 
 
    /** Determines if this header has a monitoring code.
     * 
     * @return true if there is a monitoring code.
     */
    public boolean hasMonitoring()
        {
        return _hasMonitoring;
        }
 
 
    /** Returns the monitoring code for the FIN. A 1 indicates a non-delivery warning. 
     * A 2 is for delivery notification. A 3 is for both.
     * 
     * @return the monitoring code.
     */
    public char getMonitoring()
        {
        return _monitoring;
        }
 
 
    /** Determines if there is an obsolescence code on this header.
     * 
     * @return true if there is an obsolescence.
     */
    public boolean hasObsolescence()
        {
        return _hasObsolescence;
        }
 
 
    /** Returns the period of time after which a Delayed Message Trailer is appended to a FIN user-to-user message. This is a 3-digit code.
     * 
     * @return the obsolescence code or an empty string.
     */
    public String getObsolescence()
        {
        if (_hasObsolescence)
            return _obsolescence;
        return "";
        }
 
 
    /**
     * Retrieves a properly formatted block 2 including braces and 2: identifier.
     * 
     * @return a complete block 2.
     */
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
        sb.append("I");
        sb.append(_mt);
        sb.append(getBIC12());
        sb.append(_priority);
        if (_hasMonitoring)
            {
            sb.append(_monitoring);
            if (_hasObsolescence)
                sb.append(_obsolescence);
            }
        return sb.toString();
        }
 
 
    /* (non-Javadoc)
     * @see com.chrislaforetsoftware.swift.message.ISwiftBlock#getFormattedBlock()
     */
    public String getFormattedBlock()
        {
        StringBuffer sb = new StringBuffer(32);
        sb.append("{2:");
        sb.append(getContents());
        sb.append("}");
        return sb.toString();
        }
    }