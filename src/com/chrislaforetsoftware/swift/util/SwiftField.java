/* SwiftField.java
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
 
package com.chrislaforetsoftware.swift.util;
 
import com.chrislaforetsoftware.util.Field;
 
/**
 * @author Christopher Laforet
 *
 */
 
public class SwiftField extends Field
    {
    public SwiftField(String Tag,String Value)
        {
        super(Tag,Value);
        }
     
     
    /* (non-Javadoc)
     * @see com.chrislaforetsoftware.util.Field#getFormattedField()
     */
    @Override
    public String getFormattedField()
        {
        return ":" + getTag() + ":" + getValue();
        }
    }
