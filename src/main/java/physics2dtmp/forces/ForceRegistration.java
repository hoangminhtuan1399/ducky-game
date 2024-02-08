package physics2dtmp.forces;

import physics2dtmp.rigidbody.Rigidbody2D;

// Lớp ForceRegistration là lớp đăng ký lực với một đối tượng Rigidbody2D cụ thể.
public class ForceRegistration {
    public ForceGenerator fg; // Đối tượng tạo lực (ForceGenerator).
    public Rigidbody2D rb;    // Đối tượng Rigidbody2D.

    // Constructor để khởi tạo một đối tượng ForceRegistration với ForceGenerator và Rigidbody2D tương ứng.
    public ForceRegistration(ForceGenerator fg, Rigidbody2D rb) {
        this.fg = fg;
        this.rb = rb;
    }

    // Phương thức equals được ghi đè để so sánh hai đối tượng ForceRegistration.
    @Override
    public boolean equals(Object other) {
        if (other == null) return false;
        if (other.getClass() != ForceRegistration.class) return false;

        ForceRegistration fr = (ForceRegistration)other;
        return fr.rb == this.rb && fr.fg == this.fg;
    }
}
