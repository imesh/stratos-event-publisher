#!/bin/bash
# --------------------------------------------------------------
#
# Licensed to the Apache Software Foundation (ASF) under one
# or more contributor license agreements.  See the NOTICE file
# distributed with this work for additional information
# regarding copyright ownership.  The ASF licenses this file
# to you under the Apache License, Version 2.0 (the
# "License"); you may not use this file except in compliance
# with the License.  You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing,
# software distributed under the License is distributed on an
# "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
# KIND, either express or implied.  See the License for the
# specific language governing permissions and limitations
# under the License.
#
# --------------------------------------------------------------
#
iaas=$1
host_ip="localhost"
host_port=9443

prgdir=`dirname "$0"`
script_path=`cd "$prgdir"; pwd`

set -e

if [[ -z "${iaas}" ]]; then
    echo "Usage: deploy.sh [iaas]"
    exit
fi

echo "" && echo "Adding autoscale policy..."
curl -X POST -H "Content-Type: application/json" -d "@${script_path}/autoscaling-policy-economy.json" -k -u ${AUTH_CREDENTIALS} https://${host_ip}:${host_port}/api/autoscalingPolicies

echo "" && echo "Adding network partitions..."
curl -X POST -H "Content-Type: application/json" -d "@${script_path}/network-partition-gce.json" -k -u ${AUTH_CREDENTIALS} https://${host_ip}:${host_port}/api/networkPartitions

echo "" && echo "Adding deployment policy..."
curl -X POST -H "Content-Type: application/json" -d "@${script_path}/deployment-policy-gce.json" -k -u ${AUTH_CREDENTIALS} https://${host_ip}:${host_port}/api/deploymentPolicies

echo "" && echo "Adding php cartridge..."
curl -X POST -H "Content-Type: application/json" -d "@${script_path}/cartridge_php-gce.json" -k -u ${AUTH_CREDENTIALS} https://${host_ip}:${host_port}/api/cartridges

sleep 1
echo "" && echo "Adding application policy..."
curl -X POST -H "Content-Type: application/json" -d "@${script_path}/application-policy-gce.json" -k -u ${AUTH_CREDENTIALS} https://${host_ip}:${host_port}/api/applicationPolicies

sleep 1
echo "" && echo "Adding application..."
curl -X POST -H "Content-Type: application/json" -d "@${script_path}/application_php-gce.json" -k -u ${AUTH_CREDENTIALS} https://${host_ip}:${host_port}/api/applications

sleep 1
echo "" && echo "Deploying application..."
curl -X POST -H "Content-Type: application/json" -k -u ${AUTH_CREDENTIALS} https://${host_ip}:${host_port}/api/applications/single-cartridge-app-gce/deploy/application-policy-gce
