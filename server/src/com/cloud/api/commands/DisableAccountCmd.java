/**
 *  Copyright (C) 2010 Cloud.com, Inc.  All rights reserved.
 * 
 * This software is licensed under the GNU General Public License v3 or later.
 * 
 * It is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package com.cloud.api.commands;

import org.apache.log4j.Logger;

import com.cloud.api.BaseAsyncCmd;
import com.cloud.api.BaseCmd.Manager;
import com.cloud.api.Implementation;
import com.cloud.api.Parameter;
import com.cloud.api.response.SuccessResponse;
import com.cloud.event.EventTypes;
import com.cloud.user.Account;
import com.cloud.user.UserContext;

@Implementation(method="disableAccount", manager=Manager.ManagementServer, description="Disables an account")
public class DisableAccountCmd extends BaseAsyncCmd {
	public static final Logger s_logger = Logger.getLogger(DisableAccountCmd.class.getName());
    private static final String s_name = "disableaccountresponse";

    /////////////////////////////////////////////////////
    //////////////// API parameters /////////////////////
    /////////////////////////////////////////////////////

    @Parameter(name="account", type=CommandType.STRING, required=true, description="Disables specified account.")
    private String accountName;

    @Parameter(name="domainid", type=CommandType.LONG, required=true, description="Disables specified account in this domain.")
    private Long domainId;

    /////////////////////////////////////////////////////
    /////////////////// Accessors ///////////////////////
    /////////////////////////////////////////////////////

    public String getAccountName() {
        return accountName;
    }

    public Long getDomainId() {
        return domainId;
    }

    /////////////////////////////////////////////////////
    /////////////// API Implementation///////////////////
    /////////////////////////////////////////////////////

    @Override
    public String getName() {
        return s_name;
    }

    @Override
    public String getEventType() {
        return EventTypes.EVENT_ACCOUNT_DISABLE;
    }

    @Override
    public long getAccountId() {
        Account account = (Account)UserContext.current().getAccountObject();
        if (account != null) {
            return account.getId();
        }

        return Account.ACCOUNT_ID_SYSTEM; // no account info given, parent this command to SYSTEM so ERROR events are tracked
    }

    @Override
    public String getEventDescription() {
        return  "disabling account: " + getAccountName() + " in domain: " + getDomainId();
    }

    @Override @SuppressWarnings("unchecked")
    public SuccessResponse getResponse() {
        Boolean success = (Boolean)getResponseObject();
        SuccessResponse response = new SuccessResponse();
        response.setSuccess(success);
        response.setResponseName(getName());
        return response;
    }
}
