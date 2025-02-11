/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.dolphinscheduler.dao;

import org.apache.dolphinscheduler.common.enums.AlertStatus;
import org.apache.dolphinscheduler.dao.entity.Alert;

import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class AlertDaoTest {

    @BeforeClass
    public static void setUp() {
        System.setProperty("spring.profiles.active", "h2");
    }

    @Test
    public void testAlertDao() {
        AlertDao alertDao = DaoFactory.getDaoInstance(AlertDao.class);
        Alert alert = new Alert();
        alert.setTitle("Mysql Exception");
        alert.setContent("[\"alarm time：2018-02-05\", \"service name：MYSQL_ALTER\", \"alarm name：MYSQL_ALTER_DUMP\", "
            + "\"get the alarm exception.！，interface error，exception information：timed out\", \"request address：http://blog.csdn.net/dreamInTheWorld/article/details/78539286\"]");
        alert.setAlertGroupId(1);
        alert.setAlertStatus(AlertStatus.WAIT_EXECUTION);
        alertDao.addAlert(alert);

        List<Alert> alerts = alertDao.listPendingAlerts();
        Assert.assertNotNull(alerts);
        Assert.assertNotEquals(0, alerts.size());
    }

    @Test
    public void testSendServerStopedAlert() {
        AlertDao alertDao = DaoFactory.getDaoInstance(AlertDao.class);
        int alertGroupId = 1;
        String host = "127.0.0.998165432";
        String serverType = "Master";
        alertDao.sendServerStopedAlert(alertGroupId, host, serverType);
        alertDao.sendServerStopedAlert(alertGroupId, host, serverType);
        long count = alertDao.listPendingAlerts()
                             .stream()
                             .filter(alert -> alert.getContent().contains(host))
                             .count();
        Assert.assertEquals(1L, count);
    }
}
