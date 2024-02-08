package physics2dtmp.forces;

import physics2dtmp.rigidbody.Rigidbody2D;

import java.util.ArrayList;
import java.util.List;

// Lớp ForceRegistry quản lý đăng ký lực và cập nhật các lực trên các đối tượng Rigidbody2D.
public class ForceRegistry {
    private List<ForceRegistration> registry; // Danh sách đăng ký lực.

    // Constructor để khởi tạo đối tượng ForceRegistry với một danh sách rỗng.
    public ForceRegistry() {
        this.registry = new ArrayList<>();
    }

    // Phương thức add để thêm một đăng ký lực mới với Rigidbody2D và ForceGenerator tương ứng.
    public void add(Rigidbody2D rb, ForceGenerator fg) {
        ForceRegistration fr = new ForceRegistration(fg, rb);
        registry.add(fr);
    }

    // Phương thức remove để xóa một đăng ký lực với Rigidbody2D và ForceGenerator tương ứng.
    public void remove(Rigidbody2D rb, ForceGenerator fg) {
        ForceRegistration fr = new ForceRegistration(fg, rb);
        registry.remove(fr);
    }

    // Phương thức clear để xóa tất cả các đăng ký lực.
    public void clear() {
        registry.clear();
    }

    // Phương thức updateForces để cập nhật lực trên tất cả các đối tượng đăng ký.
    public void updateForces(float dt) {
        for (ForceRegistration fr : registry) {
            fr.fg.updateForce(fr.rb, dt);
        }
    }

    // Phương thức zeroForces để đặt lực trên tất cả các đối tượng đăng ký về 0.
    public void zeroForces() {
        for (ForceRegistration fr : registry) {
            // TODO: IMPLEMENT ME
            //fr.rb.zeroForces();
        }
    }
}
