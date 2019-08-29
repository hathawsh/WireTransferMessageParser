/* FedWireIdentifier.java
 *
 * Copyright (c) 2010, Chris Laforet Software/Christopher Laforet
 * All Rights Reserved
 *
 * Started: Nov 25, 2010
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
 
/** Handles specific identifiers, splitting them into type
 * and identifier components.
 * 
 * @author Christopher Laforet
 */
 
public class FedWireIdentifier
    {
    private String _code;
    private String _identifier;
     
    /** Breaks an identifier string (Type+Identifier) into
     * an identifier.
     * 
     * @param Value the string to split.
     */
    public FedWireIdentifier(String Value)
        {
        _code = Value.substring(0,1);
         
        int index = Value.indexOf("\r");
        if (index < 0)
            _identifier = Value.substring(1).trim();
        else
            _identifier = Value.substring(1,index).trim();
        }
     
     
    public FedWireIdentifier(String Code,String Identifier)
        {
        _code = Code;
        _identifier = Identifier;
        }
     
     
    /** Retrieves the code for the ID.
     * 
     * @return returns the code portion.
     */
    public String getCode()
        {
        return _code;
        }
     
     
    /** Retrieves the identifier part of the ID.
     * 
     * @return returns the identifier portion.
     */
    public String getIdentifier()
        {
        return _identifier;
        }
     
     
    /** Retrieves the description of the code.
     * 
     * @return the description for the ID code.
     */
    public String getCodeDescription()
        {
        return FedWireMessage.lookupIDCode(_code);
        }
     
     
    /** Returns the full identifier string.
     * 
     * @return the identifier code+number.
     */
    @Override
    public String toString()
        {
        return _code + _identifier;
        }
    }
