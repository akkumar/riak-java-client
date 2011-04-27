/*
 * This file is provided to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.basho.riak.newapi.bucket;

import java.io.IOException;

import com.basho.riak.client.raw.Command;
import com.basho.riak.client.raw.RawClient;
import com.basho.riak.newapi.RiakRetryFailedException;
import com.basho.riak.newapi.cap.DefaultRetrier;
import com.basho.riak.newapi.operations.RiakOperation;

/**
 * @author russell
 * 
 */
public class FetchBucket implements RiakOperation<Bucket> {

    private final RawClient client;
    private final String bucket;

    private int retry = 0;

    /**
     * @param client
     * @param bucket
     */
    public FetchBucket(RawClient client, String bucket) {
        this.client = client;
        this.bucket = bucket;
    }

    public Bucket execute() throws RiakRetryFailedException {
        BucketProperties properties = new DefaultRetrier().attempt(new Command<BucketProperties>() {
            public BucketProperties execute() throws IOException {
                return client.fetchBucket(bucket);
            }
        }, retry);

        return new DefaultBucket(bucket, properties, client);
    }

    public FetchBucket retry(int i) {
        this.retry = i;
        return this;
    }
}
