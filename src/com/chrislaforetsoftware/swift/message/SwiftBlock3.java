/* SwiftBlock3.java
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
 
import java.util.List;
import java.util.ArrayList;
 
import com.chrislaforetsoftware.swift.util.SwiftField;
 
/** Contains a Swift Block 3.
 * 
 * @author Christopher Laforet
 */
 
public class SwiftBlock3 implements ISwiftBlock
    {
    private List<SwiftField> _fields = new ArrayList<SwiftField>();
     
    /** Package-private constructor that extracts block 3
     * contents from the contents of the block (everything between
     * the {3: and the close } non-inclusive)
     * 
     * @param Contents the block's contents
     */
    SwiftBlock3(String Contents)
        {
 
        String[] sections = Contents.split("\\{");
        if (sections != null && sections.length > 0)
            {
            for (String part : sections)
                {
                String segment = part.trim();
                if (segment.length() == 0)
                    continue;
                 
                if (!segment.endsWith("}"))
                    continue;
                 
                segment = segment.substring(0,segment.length() - 1).trim();
 
                int index = segment.indexOf(':');
                if (index >= 0)
                    {
                    if (index == 0)
                        _fields.add(new SwiftField("",segment.substring(1).trim()));
                    else if (index == segment.length())
                        _fields.add(new SwiftField(segment.substring(0,segment.length() - 1),""));
                    else
                        _fields.add(new SwiftField(segment.substring(0,index).trim(),segment.substring(index + 1).trim()));
                    }
                else
                    _fields.add(new SwiftField(segment,""));
                }
            }
        }
 
     
    /** Returns a properly formatted block 3 including braces and 3: identifier.
     * 
     * @return a complete block 3.
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
        StringBuffer sb = new StringBuffer(512);
        for (SwiftField field : _fields)
            {
            sb.append("{");
            sb.append(field.getTag());
            sb.append(":");
            sb.append(field.getValue());
            sb.append("}");
            }
        return sb.toString();
        }
 
 
    /* (non-Javadoc)
     * @see com.chrislaforetsoftware.swift.message.ISwiftBlock#getFormattedBlock()
     */
    public String getFormattedBlock()
        {
        StringBuffer sb = new StringBuffer(512);
        sb.append("{3:");
        sb.append(getContents());
        sb.append("}");
        return sb.toString();       
        }
    }
