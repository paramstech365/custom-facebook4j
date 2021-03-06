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

package facebook4j;

import facebook4j.internal.org.json.JSONObject;

import java.net.URL;
import java.util.List;

/**
 * Super interface of Facebook Response data interfaces
 * @author Ryuji Yamashita - roundrop at gmail.com
 */
public interface FacebookResponse {
    /**
     * Returns the introspection of the object if available.
     * @return introspection of the object
     */
    Metadata getMetadata();

    /**
     * Returns the JSON object used to construct this object.
     * @return the JSON object
     */
    JSONObject getJSON();

    /**
     * An interface represents introspection of objects.
     * @author Ryuji Yamashita - roundrop at gmail.com
     * @see <a href="https://developers.facebook.com/docs/reference/api/#Introspection">Graph API#Introspection - Facebook Developers</a>
     */
    interface Metadata {
        Metadata.Fields getFields();
        String getType();
        Metadata.Connections getConnections();

        interface Fields {
            List<Field> getFields();

            interface Field {
                String getName();
                String getDescription();
                String getType();
            }
        }
        
        interface Connections {
            URL getURL(String connectionName);
            List<String> getConnectionNames();
        }
    }
}
