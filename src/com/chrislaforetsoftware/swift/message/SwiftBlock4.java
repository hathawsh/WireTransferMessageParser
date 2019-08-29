/* SwiftBlock4.java
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
 
import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
 
import com.chrislaforetsoftware.swift.util.SwiftField;
 
 
/** Contains a Swift Block 4.
 * 
 * @author Christopher Laforet
 */
 
public class SwiftBlock4 implements ISwiftBlock
    {
    private List<SwiftField> _fields = new ArrayList<SwiftField>();
     
    /** Package-private constructor that parses the fields out of the 
     * block 4 contents (excluding {4: and final -}).
     * 
     * @param Contents the contents of block 4.
     */
    SwiftBlock4(String Contents)
        {
        if (Contents.length() <= 4)
            return;
 
        try
            {
            BufferedReader reader = new BufferedReader(new StringReader(Contents));
            String tag = null;
            StringBuilder value = new StringBuilder(1024);
            while (true)
                {
                String line = reader.readLine();
                if (line == null)
                    break;
                 
                if (line.startsWith(":"))
                    {
                    if (tag != null)
                        _fields.add(new SwiftField(tag,value.toString()));
                     
                    line = line.substring(1);
                    value = new StringBuilder(1024);
 
                    int index = line.indexOf(":");
                    if (index < 0)
                        tag = null;
                    else
                        {
                        tag = line.substring(0,index);
                        if (index < line.length())
                            value.append(line.substring(index + 1));
                        }
                    }
                else
                    {
                    value.append("\r\n");
                    value.append(line);
                    }
                }
 
            if (tag != null)
                _fields.add(new SwiftField(tag,value.toString()));
            }
        catch (Exception ee)
            {
            }
        }
     
     
    /** Retrieves a list of all fields in the block.
     * 
     * @return a list of the fields.
     */
    public List<SwiftField> getFields()
        {
        return _fields;
        }
     
     
    /** Attempts to retrieve the FIRST field matching the field type code.
     * 
     * @param FieldType The type code for the field.
     * @return The contents of a matching field or null if not found.
     */
    public String getField(String FieldType)
        {
        for (SwiftField field : _fields)
            {
            if (field.getTag().compareTo(FieldType) == 0)
                return field.getValue();
            }
        return null;
        }
 
 
    /**
     * Attempts to retrieve all fields matching the field type code. Some SWIFT messages repeat field elements.
     * 
     * @param FieldType The type code for the field.
     * @return An array of 0 or more Strings containing the contents of the field(s).
     */
    public String[] getFields(String FieldType)
        {
        List<String> matches = new ArrayList<String>(4);
        for (SwiftField field : _fields)
            {
            if (field.getTag().compareTo(FieldType) == 0)
                matches.add(field.getValue());
            }
 
        return matches.toArray(new String[matches.size()]);
        }
 
     
    /** Returns a properly formatted block 4 including braces and 4: identifier.
     * 
     * @return a complete block 4.
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
        StringBuffer sb = new StringBuffer(2048);
        for (SwiftField field : _fields)
            {
            if (sb.length() > 0)
                sb.append("\r\n");
            sb.append(":");
            sb.append(field.getTag());
            sb.append(":");
            sb.append(field.getValue());
            }
        return sb.toString();
        }
 
 
    /* (non-Javadoc)
     * @see com.chrislaforetsoftware.swift.message.ISwiftBlock#getFormattedBlock()
     */
    public String getFormattedBlock()
        {
        StringBuffer sb = new StringBuffer(2048);
        sb.append("{4:\r\n");
        sb.append(getContents());
        sb.append("\r\n-}");
        return sb.toString();       
        }
    }