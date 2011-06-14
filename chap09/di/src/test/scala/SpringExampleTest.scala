
package scala.book {
  import javax.annotation.Resource
  import junit.framework.Assert._
  import org.junit.Test
  import org.junit.runner.RunWith
  import org.springframework.test.context.ContextConfiguration
  import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

  @RunWith(classOf[SpringJUnit4ClassRunner])
  @ContextConfiguration(locations = Array("classpath:/application-context.xml"))
  class CalculatePriceServiceTest {

      @Resource
      var calculatePriceService: CalculatePriceService = _

      @Test
      def shouldUseCostPlusCalculatorWhenPriceTypeIsCostPlus() {
        val fakeCostPlusCalculator = new Calculator {
          def calculate(productId: String) = 2.0D
        }
        calculatePriceService.setCostPlusCalculator(fakeCostPlusCalculator)
        val price = calculatePriceService.calculate("costPlus", "some product")
        assertEquals(2.0D, price)
      }
      
      @Test
      def testShouldReturnExternalPrice() {
        val fakeExternalPriceSourceCalculator = new Calculator {
          def calculate(productId: String) = 5.0D
        }
        calculatePriceService.setExternalPriceSourceCalculator(fakeExternalPriceSourceCalculator)
        val price = calculatePriceService.calculate("externalPriceSource", "dummy")
        assertEquals(5.0D, price)
      }
  }
}