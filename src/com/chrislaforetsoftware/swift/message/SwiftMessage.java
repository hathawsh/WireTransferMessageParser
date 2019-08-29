/* SwiftMessage.java
 *
 * Copyright (c) 2010, Chris Laforet Software/Christopher Laforet
 * All Rights Reserved
 *
 * Started: Nov 29, 2010
 * Revision Information: $Date: 2010-12-01 12:11:56 $
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
 
 
/** Contains a completely parsed Swift message.
 * 
 * @author Christopher Laforet
 */
 
public class SwiftMessage
    {
    private SwiftBlock1 _block1;
    private SwiftBlock2Inbound _block2Inbound;
    private SwiftBlock2Outbound _block2Outbound;
    private SwiftBlock3 _block3;
    private SwiftBlock4 _block4;
    private SwiftBlock5 _block5;
     
    /** Creates a SWIFT message from the blocks passed in.
     * 
     * @param Block1 the parsed block 1.
     * @param Block2 the parsed block 2.
     * @param Block3 the parsed block 3 (if any).
     * @param Block4 the parsed block 4.
     * @param Block5 the parsed block 5 (if any).
     */
    public SwiftMessage(SwiftBlock1 Block1,ISwiftBlock Block2,SwiftBlock3 Block3,
        SwiftBlock4 Block4,SwiftBlock5 Block5)
        {
        _block1 = Block1;
        if (Block2 instanceof SwiftBlock2Inbound)
            _block2Inbound = (SwiftBlock2Inbound)Block2;
        else
            _block2Outbound = (SwiftBlock2Outbound)Block2;
        _block3 = Block3;
        _block4 = Block4;
        _block5 = Block5;
        }
     
     
    /** Returns the message type for this message.
     * 
     * @return the three character MT.
     */
    public String getMT()
        {
        if (_block2Inbound != null)
            return  _block2Inbound.getMT();
        return _block2Outbound.getMT();
        }
     
     
    /** Returns block 1 from the message.
     * 
     * @return block 1.
     */
    public SwiftBlock1 getBlock1()
        {
        return _block1;
        }
     
     
    /** Returns block 2 from the message if it is an inbound.
     * 
     * @return block 2.
     */
    public SwiftBlock2Inbound getBlock2Inbound()
        {
        return _block2Inbound;
        }
     
     
    /** Returns block 2 from the message if it is an outbound.
     * 
     * @return block 1.
     */
    public SwiftBlock2Outbound getBlock2Outbound()
        {
        return _block2Outbound;
        }
     
     
    /** Returns block 3 from the message.
     * 
     * @return block 3.
     */
    public SwiftBlock3 getBlock3()
        {
        return _block3;
        }
     
     
    /** Returns block 4 from the message.
     * 
     * @return block 4.
     */
    public SwiftBlock4 getBlock4()
        {
        return _block4;
        }
     
     
    /** Returns block 5 from the message.
     * 
     * @return block 5.
     */
    public SwiftBlock5 getBlock5()
        {
        return _block5;
        }
     
     
    /** Determines the direction (I or O) of this SWIFT message.
     * 
     * @return an I for an SWIFT input message or an O for an output message.
     */
    public char getDirection()
        {
        return _block2Inbound != null ? 'I' : 'O';
        }
 
 
    /** Returns the sender's BIC 12 for this message. A BIC12 is the BIC8 + LT code + Branch code.
     * 
     * @return the sender's BIC 12.
     */
    public String getSenderBIC12()
        {
        if (getDirection() == 'I')
            return _block1.getBIC12();
        else if (_block2Inbound != null)
            return _block2Inbound.getBIC12();
        return _block2Outbound.getBIC12();
        }
 
 
    /** Returns the sender's BIC 11 for this message. A BIC12 is the BIC8 + Branch code.
     * 
     * @return the sender's BIC 11.
     */
    public String getSenderBIC11()
        {
        if (getDirection() == 'I')
            return _block1.getBIC11();
        else if (_block2Inbound != null)
            return _block2Inbound.getBIC11();
        return _block2Outbound.getBIC11();
        }
 
 
    /** Returns the sender's BIC 8 for this message.
     * 
     * @return the sender's BIC 8.
     */
    public String getSenderBIC8()
        {
        if (getDirection() == 'I')
            return _block1.getBIC8();
        else if (_block2Inbound != null)
            return _block2Inbound.getBIC8();
        return _block2Outbound.getBIC8();
        }
 
 
    /** Returns the sender's LT code for this message.
     * 
     * @return the sender's LT code.
     */
    public char getSenderLT()
        {
        if (getDirection() == 'I')
            return _block1.getLT();
        else if (_block2Inbound != null)
            return _block2Inbound.getLT();
        return _block2Outbound.getLT();
        }
 
 
    /** Returns the sender's branch code for this message.
     * 
     * @return the sender's branch code.
     */
    public String getSenderBranch()
        {
        if (getDirection() == 'I')
            return _block1.getBranch();
        else if (_block2Inbound != null)
            return _block2Inbound.getBranch();
        return _block2Outbound.getBranch();
        }
 
 
    /** Returns the receiver's BIC 12 for this message. A BIC12 is the BIC8 + LT code + Branch code.
     * 
     * @return the receiver's BIC12.
     */
    public String getReceiverBIC12()
        {
        if (getDirection() == 'O')
            return _block1.getBIC12();
        else if (_block2Inbound != null)
            return _block2Inbound.getBIC12();
        return _block2Outbound.getBIC12();
        }
 
 
    /** Returns the receiver's BIC 11 for this message. A BIC12 is the BIC8 + Branch code.
     * 
     * @return the receiver's BIC11.
     */
    public String getReceiverBIC11()
        {
        if (getDirection() == 'O')
            return _block1.getBIC11();
        else if (_block2Inbound != null)
            return _block2Inbound.getBIC11();
        return _block2Outbound.getBIC11();
        }
 
 
    /** Returns the receiver's BIC 8 for this message.
     * 
     * @return the receiver's BIC 8.
     */
    public String getReceiverBIC8()
        {
        if (getDirection() == 'O')
            return _block1.getBIC8();
        else if (_block2Inbound != null)
            return _block2Inbound.getBIC8();
        return _block2Outbound.getBIC8();
        }
 
 
    /** Returns the receiver's LT code for this message.
     * 
     * @return the receiver's LT code.
     */
    public char getReceiverLT()
        {
        if (getDirection() == 'O')
            return _block1.getLT();
        else if (_block2Inbound != null)
            return _block2Inbound.getLT();
        return _block2Outbound.getLT();
        }
 
 
    /** Returns the receiver's branch code for this message.
     * 
     * @return the receiver's branch code.
     */
    public String getReceiverBranch()
        {
        if (getDirection() == 'I')
            return _block1.getBranch();
        else if (_block2Inbound != null)
            return _block2Inbound.getBranch();
        return _block2Outbound.getBranch();
        }
 
     
    /** Returns the full message formatted with CRLF between each field.
     * 
     * @return the message.
     */
    @Override
    public String toString()
        {
        StringBuilder sb  = new StringBuilder(2048);
 
        sb.append(_block1.toString());
        if (_block2Inbound != null)
            sb.append(_block2Inbound.toString());
        else
            sb.append(_block2Outbound.toString());
        if (_block3 != null)
            sb.append(_block3.toString());
        sb.append(_block4.toString());
        if (_block5 != null)
            sb.append(_block5.toString());
         
        return sb.toString();
        }
    }
