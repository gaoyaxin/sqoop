/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.sqoop.connector.jdbc.configuration;

import org.apache.sqoop.connector.jdbc.GenericJdbcConnectorConstants;
import org.apache.sqoop.model.FormClass;
import org.apache.sqoop.model.Input;
import org.apache.sqoop.validation.Status;
import org.apache.sqoop.validation.validators.Validator;

/**
 *
 */
@FormClass( validators = {FromTableForm.FormValidator.class})
public class FromTableForm {
  @Input(size = 50)
  public String schemaName;

  @Input(size = 50)
  public String tableName;

  @Input(size = 2000, validators = {SqlConditionTokenValidator.class})
  public String sql;

  @Input(size = 50)
  public String columns;

  @Input(size = 50)
  public String partitionColumn;

  @Input
  public Boolean partitionColumnNull;

  @Input(size = 50)
  public String boundaryQuery;

  public static class FormValidator extends Validator<FromTableForm> {
    @Override
    public void validate(FromTableForm form) {
      if(form.tableName == null && form.sql == null) {
        addMessage(Status.UNACCEPTABLE, "Either fromTable name or SQL must be specified");
      }
      if(form.tableName != null && form.sql != null) {
        addMessage(Status.UNACCEPTABLE, "Both fromTable name and SQL cannot be specified");
      }
      if(form.schemaName != null && form.sql != null) {
        addMessage(Status.UNACCEPTABLE, "Both schema name and SQL cannot be specified");
      }
    }
  }

  public static class SqlConditionTokenValidator extends Validator<String> {
    @Override
    public void validate(String sql) {
      if(sql != null && !sql.contains(GenericJdbcConnectorConstants.SQL_CONDITIONS_TOKEN)) {
        addMessage(Status.UNACCEPTABLE, "SQL statement must contain placeholder for auto generated conditions - " + GenericJdbcConnectorConstants.SQL_CONDITIONS_TOKEN);
      }
    }
  }
}
