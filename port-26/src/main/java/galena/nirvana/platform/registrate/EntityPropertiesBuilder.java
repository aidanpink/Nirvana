package galena.nirvana.platform.registrate;

public interface EntityPropertiesBuilder {

    EntityPropertiesBuilder sized(float width,  float height);

    EntityPropertiesBuilder clientTrackingRange(int range);

    EntityPropertiesBuilder updateInterval(int interval);

    EntityPropertiesBuilder fireImmune();

}
