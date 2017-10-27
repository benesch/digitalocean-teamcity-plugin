/*
 * Copyright 2009-2013 Cloud Castle Group
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.cloudcastlegroup.digitaloceanplugin.DigitalOceanCloudClient;
import com.cloudcastlegroup.digitaloceanplugin.DigitalOceanCloudImage;
import com.cloudcastlegroup.digitaloceanplugin.DigitalOceanCloudInstance;
import com.cloudcastlegroup.digitaloceanplugin.apiclient.DigitalOceanApiProvider;
import com.cloudcastlegroup.digitaloceanplugin.apiclient.DigitalOceanApiUtils;
import com.cloudcastlegroup.digitaloceanplugin.settings.ProfileConfigurationConstants;
import com.myjeeva.digitalocean.common.DropletStatus;
import com.myjeeva.digitalocean.pojo.Droplet;
import jetbrains.buildServer.clouds.CloudClientParameters;
import jetbrains.buildServer.clouds.CloudImage;
import jetbrains.buildServer.clouds.CloudInstanceUserData;
import jetbrains.buildServer.clouds.InstanceStatus;
import jetbrains.buildServer.util.WaitFor;

import org.apache.log4j.BasicConfigurator;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;

/**
 * User: graf
 * Date: 11/12/13
 * Time: 13:43
 */
public class DigitalOceanCloudClientTest {

  private static final String apiKey = "xxx";

  private static final String imageName = "16.04.3 x64";

  private static final String instancesLimit = "3";

  private static final String sshKeyName = "pollen-osx";

  private static final String region = "nyc3";

  private static final String size = "512mb";

  private static final String volumeSize = "40";

  @Test(enabled = false)
  public void testInstanceLifeCycle() throws InterruptedException {
	  BasicConfigurator.configure();

    Thread.sleep(10000);

    final DigitalOceanApiProvider api = new DigitalOceanApiProvider(apiKey);
    final List<Droplet> existedDroplets = api.getDroplets();

    final CloudClientParameters parameters = new CloudClientParameters();
    parameters.setParameter(ProfileConfigurationConstants.API_KEY_PROFILE_SETTING, apiKey);
    parameters.setParameter(ProfileConfigurationConstants.IMAGE_PROFILE_SETTING, imageName);
    parameters.setParameter(ProfileConfigurationConstants.INSTANCES_LIMIT_PROFILE_SETTING, instancesLimit);
    parameters.setParameter(ProfileConfigurationConstants.SSH_KEY_PROFILE_SETTING, sshKeyName);
    parameters.setParameter(ProfileConfigurationConstants.REGION_PROFILE_SETTING, region);
    parameters.setParameter(ProfileConfigurationConstants.SIZE_PROFILE_SETTING, size);
    parameters.setParameter(ProfileConfigurationConstants.VOLUME_SIZE_PROFILE_SETTING, volumeSize);

    final DigitalOceanCloudClient cloudClient = new DigitalOceanCloudClient(parameters);
    Assert.assertNull(cloudClient.getErrorInfo());

    final Collection<? extends CloudImage> cloudImages = cloudClient.getImages();

    Assert.assertEquals(cloudImages.size(), 1);

    final DigitalOceanCloudImage cloudImage = (DigitalOceanCloudImage) cloudImages.iterator().next();
    Assert.assertEquals(cloudImage.getInstances().size(), 0);
    Assert.assertTrue(cloudClient.canStartNewInstance(cloudImage));

    final DigitalOceanCloudInstance newInstance = (DigitalOceanCloudInstance) cloudClient.startNewInstance(cloudImage,
            new CloudInstanceUserData("", "", "", 30 * 1000l, "", new HashMap<String, String>()));
    Assert.assertTrue(newInstance.getStatus() == InstanceStatus.SCHEDULED_TO_START
            || newInstance.getStatus() == InstanceStatus.STARTING);
    Assert.assertEquals(cloudImage.getInstances().size(), 1);

    Thread.sleep(10000);

    Assert.assertEquals(api.getDroplets().size(), existedDroplets.size() + 1);
    Assert.assertNotNull(newInstance.getDigitalOceanDroplet());

    final int dropletId = newInstance.getDigitalOceanDroplet().getId();

    new WaitFor(2 * 60 * 1000) {
      @Override
      protected boolean condition() {
        return newInstance.getStatus() == InstanceStatus.RUNNING;
      }
    };

    Assert.assertEquals(cloudImage.getInstances().size(), 1);
    Assert.assertEquals(api.getDroplets().size(), existedDroplets.size() + 1);

    final Droplet droplet = api.getDroplet(dropletId);
    Assert.assertEquals(droplet.getStatus(), DropletStatus.ACTIVE);
    Assert.assertNotNull(DigitalOceanApiUtils.getIpAddress(droplet));

    cloudClient.terminateInstance(newInstance);

    Assert.assertEquals(cloudImage.getInstances().size(), 0);
    Assert.assertTrue(newInstance.getStatus() == InstanceStatus.STOPPED);

    Thread.sleep(10000);

    Assert.assertEquals(api.getDroplets().size(), existedDroplets.size());
  }

  @Test(enabled = false)
  public void testTwoInstancesSimultaneously() throws InterruptedException {

    Thread.sleep(10000);

    final DigitalOceanApiProvider api = new DigitalOceanApiProvider(apiKey);
    final List<Droplet> existedDroplets = api.getDroplets();

    final CloudClientParameters parameters = new CloudClientParameters();
    parameters.setParameter(ProfileConfigurationConstants.API_KEY_PROFILE_SETTING, apiKey);
    parameters.setParameter(ProfileConfigurationConstants.IMAGE_PROFILE_SETTING, imageName);
    parameters.setParameter(ProfileConfigurationConstants.INSTANCES_LIMIT_PROFILE_SETTING, instancesLimit);
    parameters.setParameter(ProfileConfigurationConstants.SSH_KEY_PROFILE_SETTING, sshKeyName);
    parameters.setParameter(ProfileConfigurationConstants.REGION_PROFILE_SETTING, region);
    parameters.setParameter(ProfileConfigurationConstants.SIZE_PROFILE_SETTING, size);
    parameters.setParameter(ProfileConfigurationConstants.VOLUME_SIZE_PROFILE_SETTING, volumeSize);

    final DigitalOceanCloudClient cloudClient = new DigitalOceanCloudClient(parameters);
    final Collection<? extends CloudImage> cloudImages = cloudClient.getImages();

    Assert.assertEquals(cloudImages.size(), 1);

    final DigitalOceanCloudImage cloudImage = (DigitalOceanCloudImage) cloudImages.iterator().next();
    Assert.assertEquals(cloudImage.getInstances().size(), 0);
    Assert.assertTrue(cloudClient.canStartNewInstance(cloudImage));

    Executors.newFixedThreadPool(2).submit(new Runnable() {
      @Override
      public void run() {
        final DigitalOceanCloudInstance newInstance = (DigitalOceanCloudInstance) cloudClient.startNewInstance(cloudImage,
                new CloudInstanceUserData("", "", "", 30 * 1000l, "", new HashMap<String, String>()));
        Assert.assertTrue(newInstance.getStatus() == InstanceStatus.SCHEDULED_TO_START
                || newInstance.getStatus() == InstanceStatus.STARTING);

        Assert.assertNotNull(newInstance.getDigitalOceanDroplet());

        final int dropletId = newInstance.getDigitalOceanDroplet().getId();

        new WaitFor(2 * 60 * 1000) {
          @Override
          protected boolean condition() {
            return newInstance.getStatus() == InstanceStatus.RUNNING;
          }
        };

        final Droplet droplet = api.getDroplet(dropletId);
        Assert.assertEquals(droplet.getStatus(), DropletStatus.ACTIVE);
        Assert.assertNotNull(DigitalOceanApiUtils.getIpAddress(droplet));

        cloudClient.terminateInstance(newInstance);

        Assert.assertTrue(newInstance.getStatus() == InstanceStatus.STOPPED);
      }
    });

    new WaitFor() {
      @Override
      protected boolean condition() {
        return cloudImage.getInstances().size() == 2;
      }
    };

    Thread.sleep(10000);

    Assert.assertEquals(api.getDroplets().size(), existedDroplets.size());
  }

}
