package palabrot.services
import munit.FunSuite
import palabrot.utils.Helper

class HelperTest extends FunSuite {

  test("Helper util return help message") {
    assert(Helper.display.startsWith("Ayuda:"))
  }
}