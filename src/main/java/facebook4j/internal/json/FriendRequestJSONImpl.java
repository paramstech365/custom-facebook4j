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

import java.util.Date;

import facebook4j.FacebookException;
import facebook4j.FriendRequest;
import facebook4j.IdNameEntity;
import facebook4j.ResponseList;
import facebook4j.conf.Configuration;
import facebook4j.internal.http.HttpResponse;
import facebook4j.internal.org.json.JSONArray;
import facebook4j.internal.org.json.JSONException;
import facebook4j.internal.org.json.JSONObject;

/**
 * @author Ryuji Yamashita - roundrop at gmail.com
 */
/*package*/ final class FriendRequestJSONImpl implements FriendRequest, java.io.Serializable {
    private static final long serialVersionUID = 6531121120616740631L;
    
    private IdNameEntity from;
    private IdNameEntity to;
    private Date createdTime;
    private String message;
    private Boolean unread;

    /*package*/FriendRequestJSONImpl(HttpResponse res, Configuration conf) throws FacebookException {
        JSONObject json = res.asJSONObject();
        init(json);
        if (conf.isJSONStoreEnabled()) {
            DataObjectFactoryUtil.clearThreadLocalMap();
            DataObjectFactoryUtil.registerJSONObject(this, json);
        }
    }

    /*package*/FriendRequestJSONImpl(JSONObject json) throws FacebookException {
        super();
        init(json);
    }

    private void init(JSONObject json) throws FacebookException {
        try {
            if (!json.isNull("from")) {
                JSONObject fromJSONObject = json.getJSONObject("from");
                from = new IdNameEntityJSONImpl(fromJSONObject);
            } else {
                from = null;
            }
            if (!json.isNull("to")) {
                JSONObject toJSONObject = json.getJSONObject("to");
                to = new IdNameEntityJSONImpl(toJSONObject);
            } else {
                to = null;
            }
            createdTime = getFacebookDatetime("created_time", json);
            message = getRawString("message", json);
            unread = getBoolean("unread", json);
        } catch (JSONException jsone) {
            throw new FacebookException(jsone.getMessage(), jsone);
        }
    }

    public IdNameEntity getFrom() {
        return from;
    }

    public IdNameEntity getTo() {
        return to;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public String getMessage() {
        return message;
    }

    public Boolean unread() {
        return unread;
    }

    /*package*/
    static ResponseList<FriendRequest> createFriendRequestList(HttpResponse res, Configuration conf) throws FacebookException {
        try {
            if (conf.isJSONStoreEnabled()) {
                DataObjectFactoryUtil.clearThreadLocalMap();
            }
            JSONObject json = res.asJSONObject();
            JSONArray list = json.getJSONArray("data");
            int size = list.length();
            ResponseList<FriendRequest> friendRequests = new ResponseListImpl<FriendRequest>(size, json);
            for (int i = 0; i < size; i++) {
                FriendRequest friendRequest = new FriendRequestJSONImpl(list.getJSONObject(i));
                friendRequests.add(friendRequest);
            }
            if (conf.isJSONStoreEnabled()) {
                DataObjectFactoryUtil.registerJSONObject(friendRequests, json);
            }
            return friendRequests;
        } catch (JSONException jsone) {
            throw new FacebookException(jsone);
        }
    }

    @Override
    public String toString() {
        return "FriendRequestJSONImpl [from=" + from + ", to=" + to
                + ", createdTime=" + createdTime + ", unread=" + unread + "]";
    }

}