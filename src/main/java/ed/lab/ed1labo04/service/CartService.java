package ed.lab.ed1labo04.service;

import ed.lab.ed1labo04.entity.CartEntity;
import ed.lab.ed1labo04.entity.CartItem;
import ed.lab.ed1labo04.entity.ProductEntity;
import ed.lab.ed1labo04.model.CartItemResponse;
import ed.lab.ed1labo04.model.CartResponse;
import ed.lab.ed1labo04.model.CreateCartRequest;
import ed.lab.ed1labo04.repository.CartRepository;
import ed.lab.ed1labo04.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final ProductService productService;
    private final ProductRepository productRepository;

    public CartService(CartRepository cartRepository,
                       ProductService productService,
                       ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.productService = productService;
        this.productRepository = productRepository;
    }

    public CartResponse createCart(CreateCartRequest createCartRequest) {
        List<CartItemResponse> cartResponseItems = new ArrayList<>();
        Map<Long, Map.Entry<ProductEntity, CartItemResponse>> cartMap = new HashMap<>();
        double totalPrice = 0.0;

        for (CartItem cartItem : createCartRequest.getCartItems()) {
            ProductEntity product = productService.getProductById(cartItem.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("Product with id " + cartItem.getId() + " not found"));

            if (cartItem.getQuantity() <= 0) {
                throw new IllegalArgumentException("Quantity must be greater than zero");
            }

            if (product.getQuantity() < cartItem.getQuantity()) {
                throw new IllegalArgumentException("Product with id " + cartItem.getId() + " has insufficient quantity");
            }

            CartItemResponse cartItemResponse = new CartItemResponse();

            cartItemResponse.setProductId(product.getId());
            cartItemResponse.setName(product.getName());
            cartItemResponse.setPrice(product.getPrice());
            cartItemResponse.setQuantity(cartItem.getQuantity());

            cartResponseItems.add(cartItemResponse);

            cartMap.put(product.getId(), Map.entry(product, cartItemResponse));

            totalPrice += product.getPrice() * cartItem.getQuantity();
        }

        CartEntity cartEntity = new CartEntity();

        cartEntity.setCartItems(createCartRequest.getCartItems());

        cartEntity = cartRepository.save(cartEntity);

        CartResponse cartResponse = new CartResponse();

        cartResponse.setId(cartEntity.getId());
        cartResponse.setCartItems(cartResponseItems);
        cartResponse.setTotalPrice(totalPrice);

        for (CartItemResponse cartItemResponse : cartResponseItems) {
            ProductEntity product = cartMap.get(cartItemResponse.getProductId()).getKey();
            CartItemResponse itemResponse = cartMap.get(product.getId()).getValue();

            product.setQuantity(product.getQuantity() - itemResponse.getQuantity());

            productRepository.save(product);
        }

        return cartResponse;
    }

    public Optional<CartResponse> getCartById(Long id) {
        return cartRepository.findById(id)
                .map(cartEntity -> {
                    CartResponse cartResponse = new CartResponse();

                    List<CartItemResponse> cartResponseItems = new ArrayList<>();
                    double totalPrice = 0.0;

                    for (CartItem cartItem : cartEntity.getCartItems()) {
                        ProductEntity product = productService.getProductById(cartItem.getId())
                                .orElseThrow(() -> new IllegalStateException("Product with id " + cartItem.getId() + " not found"));

                        CartItemResponse cartItemResponse = new CartItemResponse();

                        cartItemResponse.setName(product.getName());
                        cartItemResponse.setPrice(product.getPrice());
                        cartItemResponse.setQuantity(cartItem.getQuantity());

                        cartResponseItems.add(cartItemResponse);

                        totalPrice += product.getPrice() * cartItem.getQuantity();
                    }

                    cartResponse.setId(cartEntity.getId());
                    cartResponse.setCartItems(cartResponseItems);
                    cartResponse.setTotalPrice(totalPrice);

                    return cartResponse;
                });
    }
}
