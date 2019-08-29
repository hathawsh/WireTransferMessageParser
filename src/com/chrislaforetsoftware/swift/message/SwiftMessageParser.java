/* SwiftMessageParser.java
 *
 * Copyright (c) 2010, Chris Laforet Software/Christopher Laforet
 * All Rights Reserved
 *
 * Started: Nov 30, 2010
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
 
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
 
import com.chrislaforetsoftware.swift.util.SwiftField;
import com.chrislaforetsoftware.util.Field;
import com.chrislaforetsoftware.util.MessageParseResults;
 
/** Attempts to parse a SWIFT message into its components.
 * This does not split RJE or DOS-PCC files, it is expecting
 * a single message.  Also, this code does not handle the
 * ACK/UAK header which can be prepended to SWIFT messages.
 * 
 * @author Christopher Laforet
 */
 
public class SwiftMessageParser
    {
    /** Attempts to parse a SWIFT message from the provided
     * content and return it.  The message must be properly formed.
     * 
     * @param Contents a string containing a SWIFT message.
     * @return a parsed message if successful contained in a MessageParseResults object.
     * @throws IOException if an error occurs while parsing.
     */
    static public MessageParseResults parseMessage(String Contents) throws IOException
        {
        List<String> errorList = new ArrayList<String>();
         
        String contents = Contents;
        int block1Elements = findAndCountMatches(contents,"{1:");
        if (block1Elements < 1)
            errorList.add("Missing block 1 in message.");
        else if (block1Elements > 1)
            {
            errorList.add("Too many block 1 entries in message.  Does this have an ACK/UAK header?");
 
            // determine if we have an ACK/UAK then remove it
            int index = contents.indexOf("{1:F01");
            if (block1Elements == 2 && 
                (contents.startsWith("{1:F21") || contents.startsWith("{1:F31")) && 
                index >= 0)
                {
                contents = contents.substring(index);
                block1Elements = findAndCountMatches(contents,"{1:");       // recount block 1s
 
                if (block1Elements > 1)
                    errorList.add("SWIFT message contains more than one block 1 even after removing header.. Unable to parse.");
                }
            else
                errorList.add("SWIFT message contains more than one block 1. Unable to parse.");
            }
 
        if (block1Elements == 1)
            {
            int block2Elements = findAndCountMatches(contents,"{2:");
            int block3Elements = findAndCountMatches(contents,"{3:");
            int block4Elements = findAndCountMatches(contents,"{4:");
            int block5Elements = findAndCountMatches(contents,"{5:");
     
            boolean canParse = true;
            if (block2Elements < 1)
                errorList.add("SWIFT message is missing a block 2.");
            else if (block2Elements > 2)
                {
                canParse = true;
                errorList.add("SWIFT message contains more than one block 2.");
                }
     
            if (block4Elements < 1)
                errorList.add("SWIFT message is missing a block 4.");
            else if (block4Elements > 1)
                {
                errorList.add("SWIFT message contains more than one block 4.");
                canParse = true;
                }
            if (block3Elements > 1)
                {
                errorList.add("SWIFT message contains more than one block 3.");
                canParse = true;
                }
             
            if (block5Elements > 1)
                {
                errorList.add("SWIFT message contains more than one block 5.");
                canParse = true;
                }
     
            if (contents.indexOf("-}") < 0)
                {
                errorList.add("SWIFT message is missing a dash-curly on message.");
                canParse = true;
                }
            else if (canParse)
                {
                // make sure that block 4 is correctly started and ended without 
                // intervening open curly brace
                int block4 = contents.indexOf("{4:");
                int dashCurly = contents.indexOf("-}",block4 + 1);
                int nextBrace = contents.indexOf("{",block4 + 1);
                if (dashCurly < 0 || (nextBrace >= 0 && dashCurly > nextBrace))
                    {
                    errorList.add("SWIFT message is missing a dash-curly on block 4.");
                    canParse = true;
                    }
                }
     
            // check order of blocks is correct (1, 2, 3, 4, 5)
            if (canParse)
                {
                int block1Offset = contents.indexOf("{1:");
                int block2Offset = contents.indexOf("{2:");
                int block4Offset = contents.indexOf("{4:");
     
                if (block1Offset > block2Offset)
                    {
                    errorList.add("SWIFT message's block 1 does not occur before block 2.");
                    canParse = true;
                    }
                if (block2Offset > block4Offset)
                    {
                    errorList.add("SWIFT message's block 2 does not occur before block 4.");
                    canParse = true;
                    }
                if (block1Offset > block4Offset)
                    {
                    errorList.add("SWIFT message's block 1 does not occur before block 4.");
                    canParse = true;
                    }
     
                if (block3Elements == 1)
                    {
                    int block3Offset = contents.indexOf("{3:");
                    if (block1Offset > block3Offset)
                        {
                        errorList.add("SWIFT message's block 1 does not occur before block 3.");
                        canParse = true;
                        }
                    if (block2Offset > block3Offset)
                        {
                        errorList.add("SWIFT message's block 2 does not occur before block 3.");
                        canParse = true;
                        }
                    if (block3Offset > block4Offset)
                        {
                        errorList.add("SWIFT message's block 3 does not occur before block 4.");
                        canParse = true;
                        }
                    }
                if (block5Elements == 1)
                    {
                    int block5Offset = contents.indexOf("{5:");
                    if (block1Offset > block5Offset)
                        {
                        errorList.add("SWIFT message's block 1 does not occur before block 5.");
                        canParse = true;
                        }
                    if (block2Offset > block5Offset)
                        {
                        errorList.add("SWIFT message's block 2 does not occur before block 5.");
                        canParse = true;
                        }
                    if (block4Offset > block5Offset)
                        {
                        errorList.add("SWIFT message's block 4 does not occur before block 5.");
                        canParse = true;
                        }
                    }
                }
     
            // now to parse the contents of the Swift message's blocks
            if (canParse)
                {
                // strip the parts
                int offset1 = contents.indexOf("{1:");
                int offset2 = contents.indexOf("{2:");
                int offset3 = contents.indexOf("{3:");
                int offset4 = contents.indexOf("{4:");
                int offset5 = contents.indexOf("{5:");
     
                String block1Contents = contents.substring(offset1,offset2).trim();
                String block2Contents = null;
                String block3Contents = null;
                if (offset3 >= 0)
                    {
                    block2Contents = contents.substring(offset2,offset3).trim();
                    block3Contents = contents.substring(offset3,offset4).trim();
                    }
                else
                    block2Contents = contents.substring(offset2,offset4).trim();
                String block4Contents = null;
                String block5Contents = null;
                if (offset5 >= 0)
                    {
                    block4Contents = contents.substring(offset4,offset5).trim();
                    block5Contents = contents.substring(offset5).trim();
                    }
                else
                    block4Contents = contents.substring(offset4).trim();
     
                try
                    {
                    SwiftBlock1 block1 = new SwiftBlock1(block1Contents.substring(3,block1Contents.length() - 1));
                     
                    ISwiftBlock block2 = null;
                    if (block2Contents.charAt(3) == 'I')
                        block2 = new SwiftBlock2Inbound(block2Contents.substring(3,block2Contents.length() - 1));
                    else if (block2Contents.charAt(3) == 'O')
                        block2 = new SwiftBlock2Outbound(block2Contents.substring(3,block2Contents.length() - 1));
                    else
                        throw new Exception("SWIFT message has an invalid block 2: Neither I nor O.");
                     
                    SwiftBlock3 block3 = null;
                    if (block3Contents != null)
                        block3 = new SwiftBlock3(block3Contents.substring(3,block3Contents.length() - 1));
                     
                    SwiftBlock4 block4 = new SwiftBlock4(block4Contents.substring(5,block4Contents.length() - 2));
                     
                    SwiftBlock5 block5 = null;
                    if (block5Contents != null)
                        block5 = new SwiftBlock5(block5Contents.substring(3,block5Contents.length() - 1));
                     
                    SwiftMessage msg = new SwiftMessage(block1,block2,block3,block4,block5);
                     
                    List<Field> fields = new ArrayList<Field>();
                    for (SwiftField field : block4.getFields())
                        fields.add(field);
                    return new MessageParseResults(msg,fields,errorList);
                    }
                catch (Exception ee)
                    {
                    errorList.add("SWIFT message parser failed with an exception: " + ee.getMessage() + ".");
                    }
                }
            }
         
//FedWireMessage message = new FedWireMessage(fieldList,support.errorList);
        return new MessageParseResults(null,new ArrayList<Field>(),errorList);
        }
 
     
    /** Counts the number of occurrences of the needle pattern in the
     * haystack.
     * 
     * @param Haystack the string to search.
     * @param Needle the pattern to find.
     * @return the total number of occurrences of the needle in the haystack.
     */
    static private int findAndCountMatches(String Haystack,String Needle)
        {
        int total = 0;
        for (int offset = -1; offset < Haystack.length();)
            {
            offset = Haystack.indexOf(Needle,offset + 1);
            if (offset < 0)
                break;
            ++total;
            }
 
        return total;
        }
    }
