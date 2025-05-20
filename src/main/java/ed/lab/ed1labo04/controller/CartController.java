package ed.lab.ed1labo04.controller;

import ed.lab.ed1labo04.model.CartResponse;
import ed.lab.ed1labo04.model.CreateCartRequest;
import ed.lab.ed1labo04.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping
    public ResponseEntity<CartResponse> createCart(@RequestBody CreateCartRequest createCartRequest) {
        try {
            return new ResponseEntity(cartService.createCart(createCartRequest), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<CartResponse> getCart(@PathVariable long id) {
        return cartService.getCartById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}
