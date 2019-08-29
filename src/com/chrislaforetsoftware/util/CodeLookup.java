/* CodeLookup.java
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
 
/** Container for a code and its description.
 * 
 * @author Christopher Laforet
 */
 
public class CodeLookup
    {
    private String _code;
    private String _description;
     
    public CodeLookup(String Code,String Description)
        {
        _code = Code;
        _description = Description;
        }
     
    /** Derives the code in this lookup node.
     * 
     * @return the code.
     */
    public String getCode()
        {
        return _code;
        }
     
     
    /** Retrieves the description for the code in this node.
     * 
     * @return the descriptive text
     */
    public String getDescription()
        {
        return _description;
        }
    }