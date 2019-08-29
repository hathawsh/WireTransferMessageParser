/* MessageParser.java
 *
 * Copyright (c) 2010, Chris Laforet Software/Christopher Laforet
 * All Rights Reserved
 *
 * Started: Nov 23, 2010
 * Revision Information: $Date$
 *                       $Revision$
 * 
 * 
 * The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http: * www.mozilla.org/MPL/
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
 
package com.chrislaforetsoftware.fed.message;
 
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
 
import com.chrislaforetsoftware.fed.util.FedWireField;
import com.chrislaforetsoftware.util.Field;
import com.chrislaforetsoftware.util.MessageParseResults;
import com.chrislaforetsoftware.util.ParseSupport;
 
 
/** Contains the logic to parse a FedWire message.
 * 
 * @author Christopher Laforet
 */
 
public class FedWireMessageParser
    {
    /** Attempts to parse a FedWire message from the provided
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
 
        checkFields(fieldList,support);
         
         
// TODO: form the message
        FedWireMessage message = new FedWireMessage(fieldList,support.errorList);
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
        int offset = Line.indexOf("{");
        if (offset < 0)
            {
            Support.errorList.add("Line " + Support.lineNumber + ": Missing open curly ({) on tag number.");
            return null;
            }
        else if (offset > 0)
            Support.errorList.add("Line " + Support.lineNumber + ": Incorrectly placed open curly ({) on tag number...not first character on line.");
        int tagStart = offset + 1;
 
        offset = Line.indexOf("}",offset);
        if (offset < 0)
            {
            Support.errorList.add("Line " + Support.lineNumber + ": Missing close curly (}) on tag number.");
            return null;
            }
         
        String tag = Line.substring(tagStart,offset);
        if (tag.length()!= 4)
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
        if (offset == Line.length() - 1)
            Support.errorList.add("Line " + Support.lineNumber + ": No data follows the tag...empty value field.");
        else
            contents.append(Line.substring(offset + 1));
         
        while (true)
            {
            Support.reader.mark(1);
            int nextChar = Support.reader.read();
            Support.reader.reset();
            if (nextChar == -1)
                break;
            if ((char)nextChar == '{')
                break;
             
            String line = Support.reader.readLine();
            Support.lineNumber++;
             
            contents.append("\r\n");
            contents.append(line);
            }
         
        return new FedWireField(tag,contents.toString());
        }
     
     
    /** Checks the fields for valid form according to the specs.
     * 
     * @param Fields the list of fields to check.
     * @param Support the container containing the error lists.
     */
    static private void checkFields(List<Field> Fields,ParseSupport Support)
        {
        for (Field field : Fields)
            {
            int tag = -1;
            try
                {
                tag = Integer.parseInt(field.getTag());
                }
            catch (Exception ee)
                {
                }
             
            switch (tag)
                {
                case 1500:
                    if (field.getValue().length() != 11 && field.getValue().length() != 12)
                        Support.errorList.add("Malformed field: " + field.getTag() + " incorrect length.");
                    break;
                     
                case 1510:
                    if (field.getValue().length() != 4)
                        Support.errorList.add("Malformed field: " + field.getTag() + " incorrect length.");
                    break;
                 
                case 1520:
                    if (field.getValue().length() != 22)
                        Support.errorList.add("Malformed field: " + field.getTag() + " (IMAD) incorrect length.");
                    break;
                 
                case 2000:
                    if (field.getValue().length() != 12)
                        Support.errorList.add("Malformed field: " + field.getTag() + " (Amount) incorrect length.");
                    break;
                 
                case 3100:
                    if (field.getValue().length() < 9)
                        Support.errorList.add("Malformed field: " + field.getTag() + " incorrect length.");
                    break;
                 
                case 3400:
                    if (field.getValue().length() < 9)
                        Support.errorList.add("Malformed field: " + field.getTag() + " incorrect length.");
                    break;
                 
                case 3600:
                    if (field.getValue().length() != 3 && field.getValue().compareTo("CTRCOV") != 0)
                        Support.errorList.add("Malformed field: " + field.getTag() + " incorrect length.");
                    break;
                 
                case 3000:
                    if (field.getValue().length() != 10)
                        Support.errorList.add("Malformed field: " + field.getTag() + " incorrect length.");
                    break;
                 
                case 3320:
                    if (field.getValue().length() != 16)
                        Support.errorList.add("Malformed field: " + field.getTag() + " incorrect length.");
                    break;
                 
                case 3500:
                    if (field.getValue().length() != 22)
                        Support.errorList.add("Malformed field: " + field.getTag() + " (Prev IMAD) incorrect length.");
                    break;
                 
                case 3700:
                    if (field.getValue().length() != 15)
                        Support.errorList.add("Malformed field: " + field.getTag() + " incorrect length.");
                    break;
                 
                case 3710:
                    if (field.getValue().length() != 18)
                        Support.errorList.add("Malformed field: " + field.getTag() + " incorrect length.");
                    break;
                 
                case 3720:
                    if (field.getValue().length() != 12)
                        Support.errorList.add("Malformed field: " + field.getTag() + " incorrect length.");
                    break;
                 
                case 4000:
                case 4100:
                case 4200:
                case 4320:
                case 4400:
                case 5000:
                case 5100:
                case 5200:
                case 5400:
                case 6000:
                case 6100:
                case 6110:
                case 6200:
                case 6210:
                case 6300:
                case 6310:
                case 6400:
                case 6410:
                case 6420:
                case 6430:
                case 6500:
                case 9000:
                    if (field.getValue().length() == 0)
                        Support.errorList.add("Malformed field: " + field.getTag() + " is empty.");
                    break;
 
                case 1100:
                case 1110:
                case 1120:
                case 1130:
                    if (field.getValue().length() == 0)
                        Support.errorList.add("Malformed field: " + field.getTag() + " is empty.");
                    break;
 
                default:
                    Support.errorList.add("Invalid field type: " + field.getTag() + " found in list.");
                }
            }
        }
    }
