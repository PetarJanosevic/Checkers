package checkers.hosting;

import checkers.hosting.interfaces.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.Optional;


/**
 * Represents the cache of instantiated services in the application.
 */
public class Cache {

    private List<Service> services;

    public Cache() {
        services = new ArrayList<>();
    }

    public Optional<Service> getService(String searchedServiceName) {
        for (Service service : services) {
            if (service.getName().equalsIgnoreCase(searchedServiceName)) {
                return Optional.ofNullable(service);
            }
        }
        return Optional.empty();
    }

    public Optional<Service> addService(Service newService) {
        if (!isServiceCached(newService)) {
            services.add(newService);
            return Optional.ofNullable(newService);
        }
        return Optional.empty();
    }

    private Boolean isServiceCached(Service searchedService) {
        for (Service service : services) {
            if (service.getName().equalsIgnoreCase(searchedService.getName())) {
                return true;
            }
        }
        return false;
    }

    public void insertOrReplaceService(Service newService) {
        //Is current ServiceType already instantiated? => Remove it.
        if (isServiceCached(newService)) {
            Iterator<Service> iterator = services.iterator();
            while (iterator.hasNext()) {
                Service service = iterator.next();
                if (service.getName().equalsIgnoreCase(newService.getName())) {
                    iterator.remove();
                }
            }
        }
        addService(newService);
    }

    public void removeService(Service serviceToRemove) {
        if (isServiceCached(serviceToRemove)) {
            Iterator<Service> iterator = services.iterator();
            while (iterator.hasNext()) {
                Service service = iterator.next();
                if (service.getName().equalsIgnoreCase(serviceToRemove.getName())) {
                    iterator.remove();
                }
            }
        }
    }
}
