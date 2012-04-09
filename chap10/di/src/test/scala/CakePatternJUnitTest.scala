
package scala.book.cakepatterntest {
  import junit.framework.Assert._
  import org.junit.Test
  import cakepattern._

  class CalculatePriceServiceTest extends TestPricingSystem {

      @Test
      def shouldUseCostPlusCalculatorWhenPriceTypeIsCostPlus() {
        val calculatePriceService = new CalculatePriceService
        val price = calculatePriceService.calculate("costPlus", "some product")
        assertEquals(5.0D, price)
      }
      
      @Test
      def shouldUseExternalPriceSourceCalculator() {
        val calculatePriceService = new CalculatePriceService
        val price = calculatePriceService.calculate("externalPriceSource", "dummy")
        assertEquals(10.0D, price)
      }
  }
}