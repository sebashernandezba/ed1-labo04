package ed.lab.ed1labo04.model;

import ed.lab.ed1labo04.entity.CartItem;
import ed.lab.ed1labo04.entity.ProductEntity;

import java.util.List;

public class CreateCartRequest {

    private List<CartItem> cartItems;

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

}
