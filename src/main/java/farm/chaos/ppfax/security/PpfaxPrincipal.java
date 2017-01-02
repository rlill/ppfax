package farm.chaos.ppfax.security;

import java.security.Principal;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

/**
 * The class has to define hashCode() and equals(), see interface.
 */
public class PpfaxPrincipal implements Principal {

    private final String name;
    private final String customerName;

    public PpfaxPrincipal(String name, String customerName) {
        Validate.isTrue(StringUtils.isNotBlank(name));
        Validate.isTrue(StringUtils.isNotBlank(customerName));
        this.name = name;
        this.customerName = customerName;
    }

    @Override
    public String getName() {
        return name;
    }

    public String getCustomerName() {
        return customerName;
    }

    @Override
    public String toString() {
        return "PpfaxPrincipal{" +
                "customerName='" + customerName + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PpfaxPrincipal that = (PpfaxPrincipal) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(customerName, that.customerName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, customerName);
    }

}
