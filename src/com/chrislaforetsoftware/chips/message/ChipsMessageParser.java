/* ChipsMessageParser.java
 *
 * Copyright (c) 2010, Chris Laforet Software/Christopher Laforet
 * All Rights Reserved
 *
 * Started: Nov 26, 2010
 * Revision Information: $Date$
 *                       $Revision$
 * 
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
 
package com.chrislaforetsoftware.chips.message;
 
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
 
import com.chrislaforetsoftware.chips.util.ChipsField;
import com.chrislaforetsoftware.util.Field;
import com.chrislaforetsoftware.util.MessageParseResults;
import com.chrislaforetsoftware.util.ParseSupport;
 
 
/** Contains the parser for Chips messages.
 * 
 * @author Christopher Laforet
 */
 
public class ChipsMessageParser
    {
    /** Attempts to parse a CHIPS message from the provided
     * content and return it.  The message must be properly formed.
     * 
     * @param Contents a string containing a FedWire message.
     * @return a parsed message if successful contained in a MessageParseResults object.
     * @throws IOException if an error occurs while parsing.
     */
    static public MessageParseResults parseMessage(String Contents) throws IOException
        {
        ParseSupport support = new ParseSupport(new BufferedReader(new StringReader(Contents)));
        List<Field> fieldList = new ArrayList<Field>(30);
        while (true)
            {
            String nextLine = support.reader.readLine();
            if (nextLine == null)
                break;
            ++support.lineNumber;
             
            if (nextLine.trim().length() == 0)
                break;
             
            Field field = extractField(nextLine,support);
            if (field != null)
                fieldList.add(field);
            else
                support.errorList.add("Line " + support.lineNumber + ": Invalid field or malformed field tag");
            }
 
//      checkFields(fieldList,support);
         
        ChipsMessage message = new ChipsMessage(fieldList,support.errorList);
        return new MessageParseResults(message,fieldList,support.errorList);
        }
     
     
    /** Handles parsing the tag out of the current line and 
     * checks to see if the tag wraps onto other lines. If so,
     * it reads the wrapped lines.  It does not check for field
     * legality.
     * 
     * @param Line the first (or only) line of the tagged field.
     * @param Support the parser support class containing the message to parse.
     * @return a Field object containing tag and field data or null if malformed line.
     * @throws IOException if an error occurs reading from the parsing object.
     */
    static private Field extractField(String Line,ParseSupport Support) throws IOException
        {
        int offset = Line.indexOf("[");
        if (offset < 0)
            {
            Support.errorList.add("Line " + Support.lineNumber + ": Missing open curly ([) on tag number.");
            return null;
            }
        else if (offset > 0)
            Support.errorList.add("Line " + Support.lineNumber + ": Incorrectly placed open curly (]) on tag number...not first character on line.");
        int tagStart = offset + 1;
 
        offset = Line.indexOf("]",offset);
        if (offset < 0)
            {
            Support.errorList.add("Line " + Support.lineNumber + ": Missing close curly (]) on tag number.");
            return null;
            }
         
        String tag = Line.substring(tagStart,offset);
        if (tag.length() != 3)
            Support.errorList.add("Line " + Support.lineNumber + ": Tag is invalid length of " + tag.length() + " characters instead of 4.");
        for (char ch : tag.toCharArray())
            {
            if (!Character.isDigit(ch))
                {
                Support.errorList.add("Line " + Support.lineNumber + ": Tag contains one or more invalid non-numeric characters.");
                break;
                }
            }
         
        StringBuilder contents = new StringBuilder(256);
        if (offset == Line.length() - 2)
            Support.errorList.add("Line " + Support.lineNumber + ": No data follows the tag...empty value field.");
        else
            contents.append(Line.substring(offset + 2));
         
        while (true)
            {
            Support.reader.mark(1);
            int nextChar = Support.reader.read();
            Support.reader.reset();
            if (nextChar == -1)
                break;
            if ((char)nextChar == '[')
                break;
             
            String line = Support.reader.readLine();
            Support.lineNumber++;
             
            contents.append("\r\n");
            contents.append(line);
            }
         
        return new ChipsField(tag,contents.toString());
        }
    }
