package galena.nirvana.world.entity;

public interface ICustomCreeper {

    /**
     * @return whether the default explosion logic should be prevented
     */
    boolean customExplode(double x, double y, double z, float radius);

}
