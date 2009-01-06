/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.co.md87.evetool.api;

import java.sql.Connection;

/**
 *
 * @author chris
 */
public class EveApi {

    private final Connection sqlConnection;

    public EveApi(Connection sqlConnection) {
        this.sqlConnection = sqlConnection;
    }

}
