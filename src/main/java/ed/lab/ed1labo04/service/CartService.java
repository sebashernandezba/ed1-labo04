package ed.lab.ed1labo04.service;

import ed.lab.ed1labo04.entity.CartEntity;
import ed.lab.ed1labo04.entity.CartItemEntity;
import ed.lab.ed1labo04.entity.ProductEntity;
import ed.lab.ed1labo04.model.CartItemRequest;
import ed.lab.ed1labo04.model.CreateCartRequest;
import ed.lab.ed1labo04.repository.CartRepository;
import ed.lab.ed1labo04.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {

    private final ProductRepository productRepository;
    private final CartRepository cartRepository;

    public CartService(ProductRepository productRepository, CartRepository cartRepository) {
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
    }

    public CartEntity createCart(CreateCartRequest request) {
        List<CartItemEntity> cartItems = new ArrayList<>();
        double total = 0;

        for (CartItemRequest itemRequest : request.getCartItems()) {
            ProductEntity product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("Product not found"));

            if (itemRequest.getQuantity() <= 0) {
                throw new IllegalArgumentException("Quantity must be greater than 0");
            }

            if (product.getQuantity() < itemRequest.getQuantity()) {
                throw new IllegalArgumentException("Not enough inventory for product: " + product.getName());
            }

            // Restar del inventario
            product.setQuantity(product.getQuantity() - itemRequest.getQuantity());
            productRepository.save(product);

            CartItemEntity item = new CartItemEntity();
            item.setProductId(product.getId());
            item.setName(product.getName());
            item.setPrice(product.getPrice());
            item.setQuantity(itemRequest.getQuantity());

            total += product.getPrice() * itemRequest.getQuantity();
            cartItems.add(item);
        }

        CartEntity cart = new CartEntity();
        cart.setCartItems(cartItems);
        cart.setTotalPrice(total);
        return cartRepository.save(cart);
    }

    public CartEntity getCartById(Long id) {
        return cartRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found"));
    }
}
