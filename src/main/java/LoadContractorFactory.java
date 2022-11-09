public class LoadContractorFactory {
    public LoadContractor create() {
        return new LoadContractor(new FileResourceProvider(), new SystemOutMessagePrinter());
    }
}
