package ed.lab.ed1labo04.model;

import ed.lab.ed1labo04.entity.CartItem;

import java.util.List;

public class CartResponse {

    private Long id;
    private List<CartItemResponse> cartItems;
    private Double totalPrice;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<CartItemResponse> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItemResponse> cartItems) {
        this.cartItems = cartItems;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
