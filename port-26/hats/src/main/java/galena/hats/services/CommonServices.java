package galena.hats.services;

public class CommonServices {

    public static final INetwork NETWORK = CoreServices.load(INetwork.class);
    public static final IRenderHelper RENDER_HELPER = CoreServices.load(IRenderHelper.class);

}
