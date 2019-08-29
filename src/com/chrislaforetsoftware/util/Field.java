/* Field.java
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
 
package com.chrislaforetsoftware.util;
 
/** Container for a field code and its contents.
 * 
 * @author Christopher Laforet
 */
 
public abstract class Field
    {
    private String _tag;
    private String _value;
     
    public Field(String Tag,String Value)
        {
        _tag = Tag;
        _value = Value;
        }
     
     
    /** Retrieves the tag for the field without the curly braces.
     * 
     * @return the tag for the field.
     */
    public String getTag()
        {
        return _tag;
        }
     
     
    /** Retrieves the value for the field.
     * 
     * @return the value.
     */
    public String getValue()
        {
        return _value;
        }
     
     
    /** Retrieves the field formatted correctly.
     * 
     * @return the formatted field.
     */
    public abstract String getFormattedField();
     
     
    /** Retrieves the field formatted correctly.
     * 
     * @return the formatted field.
     */
    @Override
    public String toString()
        {
        return getFormattedField();
        }
    }
    