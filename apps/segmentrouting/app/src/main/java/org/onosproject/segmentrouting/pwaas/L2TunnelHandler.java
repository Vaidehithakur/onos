/*
 * Copyright 2016-present Open Networking Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.onosproject.segmentrouting.pwaas;

import java.util.List;
import java.util.Set;

public interface L2TunnelHandler {
    void init();

    /**
     * Combines policies and tunnels to create descriptions.
     *
     * @param pending if it is true return pending to be installed pseudowires
     *                from the appropriate store, else return installed pseudowires
     * @return Set of l2 tunnel descriptions.
     */
    Set<L2TunnelDescription> getL2Descriptions(boolean pending);

    /**
     * Returns a copy of the l2 policies that exist in the store.
     *
     * @return The l2 policies
     */
    List<L2TunnelPolicy> getL2Policies();

    /**
     * Returns a copy of the l2 tunnels that exist in the store.
     *
     * @return The l2 tunnels.
     */
    List<L2Tunnel> getL2Tunnels();

    /**
     * Returns a copy of the pending l2 policies that exist in the store.
     *
     * @return The l2 policies
     */
    List<L2TunnelPolicy> getL2PendingPolicies();

    /**
     * Helper function to handle the pw removal.
     * <p>
     * This method should for the mastership of the device because it is
     * used only from network configuration updates, thus we only want
     * one instance only to program each pseudowire.
     *
     * @param pwToRemove the pseudo wires to remove
     * @deprecated onos-1.12 Do not use this method.
     */
    @Deprecated
    void tearDown(Set<L2TunnelDescription> pwToRemove);

    /**
     * Returns a copy of the pending l2 tunnels that exist in the store.
     *
     * @return The l2 tunnels.
     */
    List<L2Tunnel> getL2PendingTunnels();

    /**
     * Pwaas pipelines.
     */
    enum Pipeline {
        /**
         * The initiation pipeline.
         */
        INITIATION, /**
         * The termination pipeline.
         */
        TERMINATION
    }

    /**
     * Enum helper to carry results of various operations.
     */
    enum Result {
        /**
         * Happy ending scenario.
         */
        SUCCESS(0, "No error occurred"),

        /**
         * We have problems with the supplied parameters.
         */
        WRONG_PARAMETERS(1, "Wrong parameters"),

        /**
         * We have an internal error during the deployment
         * or removal phase.
         */
        INTERNAL_ERROR(3, "Internal error"),

        /**
         *
         */
        PATH_NOT_FOUND(7, "Could not find valid path between connection points!"),

        /**
         *
         */
        CONFIGURATION_ERROR(8, "Conflicting pseudowire configurations!");

        private final int code;
        private final String description;
        private String specificError;
        public int nextId;

        Result(int code, String description) {
            this.code = code;
            this.description = description;
        }

        public Result appendError(String error) {
           this.specificError = error;
           return this;
        }

        public String getSpecificError() {
            return specificError;
        }

        public String getDescription() {
            return description;
        }

        @Override
        public String toString() {
            return code + ": " + description;
        }
    }

    /**
     * Enum helper for handling the direction of the pw.
     */
    enum Direction {
        /**
         * The forward direction of the pseudo wire.
         */
        FWD, /**
         * The reverse direction of the pseudo wire.
         */
        REV
    }
}
