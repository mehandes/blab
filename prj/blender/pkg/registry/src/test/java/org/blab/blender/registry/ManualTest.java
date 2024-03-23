package org.blab.blender.registry;

import org.junit.Test;

import java.util.logging.Logger;

public class ManualTest {
  @Test
  public void test() {
    SchemeController schemeController = ControllerFactory.getSchemeController();
    Logger.getAnonymousLogger().info(schemeController.findAll().toString());
  }
}
