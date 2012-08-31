/*
 * Copyright 2012 Ryuji Yamashita
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package facebook4j.internal.json;

import static facebook4j.internal.util.z_F4JInternalParseUtil.*;
import facebook4j.Account;
import facebook4j.FacebookException;
import facebook4j.ResponseList;
import facebook4j.conf.Configuration;
import facebook4j.internal.http.HttpResponse;
import facebook4j.internal.org.json.JSONArray;
import facebook4j.internal.org.json.JSONException;
import facebook4j.internal.org.json.JSONObject;

/**
 * @author Ryuji Yamashita - roundrop at gmail.com
 */
/*package*/ final class AccountJSONImpl implements Account, java.io.Serializable {
    private static final long serialVersionUID = 1311323662169212743L;

    private String name;
    private String accessToken;
    private String category;
    private String id;

    /*package*/AccountJSONImpl(HttpResponse res, Configuration conf) throws FacebookException {
        JSONObject json = res.asJSONObject();
        init(json);
        if (conf.isJSONStoreEnabled()) {
            DataObjectFactoryUtil.clearThreadLocalMap();
            DataObjectFactoryUtil.registerJSONObject(this, json);
        }
    }

    /*package*/AccountJSONImpl(JSONObject json) {
        super();
        init(json);
    }

    private void init(JSONObject json) {
        name = getRawString("name", json);
        if (!json.isNull("access_token")) {
            accessToken = getRawString("access_token", json);
        }
        category = getRawString("category", json);
        id = getRawString("id", json);
    }

    public String getName() {
        return name;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getCategory() {
        return category;
    }

    public String getId() {
        return id;
    }

    /*package*/
    static ResponseList<Account> createAccountList(HttpResponse res, Configuration conf) throws FacebookException {
        try {
            if (conf.isJSONStoreEnabled()) {
                DataObjectFactoryUtil.clearThreadLocalMap();
            }
            JSONObject json = res.asJSONObject();
            JSONArray list = json.getJSONArray("data");
            int size = list.length();
            ResponseList<Account> accounts = new ResponseListImpl<Account>(size, json);
            for (int i = 0; i < size; i++) {
                Account account = new AccountJSONImpl(list.getJSONObject(i));
                accounts.add(account);
            }
            if (conf.isJSONStoreEnabled()) {
                DataObjectFactoryUtil.registerJSONObject(accounts, json);
            }
            return accounts;
        } catch (JSONException jsone) {
            throw new FacebookException(jsone);
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AccountJSONImpl other = (AccountJSONImpl) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "AccountJSONImpl [name=" + name + ", accessToken=" + accessToken
                + ", category=" + category + ", id=" + id + "]";
    }

}