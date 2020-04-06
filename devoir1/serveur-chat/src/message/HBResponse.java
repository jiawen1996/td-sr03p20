package message;

import java.io.Serializable;

public class HBResponse implements Serializable {
	public String toString() {
        return new String ("ACK");
    }
}
