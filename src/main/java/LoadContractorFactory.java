public class LoadContractorFactory {
    public LoadContractor create(Environment environment) {
        return new LoadContractor(new FileResourceProvider(), new SystemOutMessagePrinter());
    }
}
