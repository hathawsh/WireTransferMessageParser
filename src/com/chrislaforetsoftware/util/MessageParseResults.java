/* MessageParseResults.java
 *
 * Copyright (c) 2010, Chris Laforet Software/Christopher Laforet
 * All Rights Reserved
 *
 * Started: Nov 24, 2010
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
 
import java.util.List;
 
/** Container for a parse operation on a message.  The 
 * message is attempted to be returned along with tag-value
 * pairs and any errors encountered during the process.
 * 
 * @author Christopher Laforet
 */
 
public class MessageParseResults
    {
    private Object _message;
    private List<Field> _fields;
    private List<String> _errors;
 
    public MessageParseResults(Object Message,List<Field> Fields,List<String> Errors)
        {
        _message = Message;
        _fields = Fields;
        _errors = Errors;
        }
     
    /** Retrieves the message object if it exists or null
     * otherwise.
     * 
     * @return the message object or null.
     */
    public Object getMessage()
        {
        return _message;
        }
     
     
    /** Returns the number of fields in the parsed data.
     * 
     * @return the number of fields parsed.
     */
    public int fieldCount()
        {
        return _fields.size();
        }
     
    /** Retrieves the list of parsed fields for the
     * message.
     * 
     * @return a list of zero or more Field objects.
     */
    public List<Field> getFields()
        {
        return _fields;
        }
     
     
    /** Returns the number of errors in the parse.
     * 
     * @return the number of errors.
     */
    public int errorCount()
        {
        return _errors.size();
        }
     
    /** Retrieves the list of errors encountered during
     * the parsing process.
     * 
     * @return a list of errors.
     */
    public List<String> getErrors()
        {
        return _errors;
        }
    }
    