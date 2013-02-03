// ______________________________________________________________________________
//
//           Project:
//              File: $Id: VersionTest.java 384 2012-12-02 21:56:12Z ifw10191 $
//      last changed: $Rev: 384 $
// ______________________________________________________________________________
//
//        created by: ${USER}
//     creation date: ${DATE}
//        changed by: $Author: ifw10191 $
//       change date: $LastChangedDate: 2012-12-02 22:56:12 +0100 (So, 02. Dez 2012) $
//       description:
// ______________________________________________________________________________
//
//         Copyright: (c) Christian Fritz, all rights reserved
// ______________________________________________________________________________
package edu.hm.gamedev.server;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class VersionTest {

  @Test
  public void testGetBuildNumber() throws Exception {
    assertEquals("#0", Version.getBuildNumber());
  }

  @Test
  public void testGetBuildRevision() throws Exception {
    assertEquals("-1", Version.getBuildRevision());
  }

  @Test
  public void testGetBuildDate() throws Exception {
    assertEquals("1970-01-01_00:00:00", Version.getBuildDate());
  }
}
