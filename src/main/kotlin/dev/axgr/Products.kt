package dev.axgr

import org.springframework.core.convert.converter.Converter
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.*

@Service
class ProductService {

  private val products = listOf(
    Product("79a9a1ef-2661-4ebd-909e-604b12c7c20d", "Laptop"),
    Product("d0274f05-8dd3-4ed0-a23d-8a5d5d666412", "Camera"),
    Product("88b2aad0-7c8f-4f17-839f-cf4100abed1d", "Keyboard")
  )

  fun products(): List<Product> = products
}

@RestController
@RequestMapping("/products")
class ProductController(private val service: ProductService) {

  @GetMapping
  fun all(): List<Product> = service.products()

  @GetMapping("/{id}")
  fun find(@PathVariable("id") product: Product): Product {
    return product
  }
}

data class Product(val id: String, val name: String)

@ResponseStatus(code = HttpStatus.NOT_FOUND)
class ProductNotFound(id: String) : RuntimeException("Product with id $id not found")

@Component
class ProductConverter(private val products: ProductService) : Converter<String, Product> {

  override fun convert(source: String): Product {
    return products.products().find { it.id == source } ?: throw ProductNotFound(source)
  }
}
