/* SwiftBlock5.java
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
 
/** Contains a Swift Block 5.  Block 5 is similar
 * enough to block 3 that it merely extends it.  Block 5
 * contains tag:value pairs within curly braces.
 * 
 * @author Christopher Laforet
 */
 
public class SwiftBlock5 extends SwiftBlock3
    {
    SwiftBlock5(String Contents)
        {
        super(Contents);
        }
     
     
    /* (non-Javadoc)
     * @see com.chrislaforetsoftware.swift.message.ISwiftBlock#getFormattedBlock()
     */
    @Override
    public String getFormattedBlock()
        {
        StringBuffer sb = new StringBuffer(512);
        sb.append("{5:");
        sb.append(getContents());
        sb.append("}");
        return sb.toString();       
        }
 
    }