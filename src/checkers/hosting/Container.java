package checkers.hosting;

import checkers.core.board.*;
import checkers.core.gui.*;
import checkers.core.logic.*;
import checkers.hosting.interfaces.*;

import java.util.Optional;

/**
 * Instantiates and manages all services of the application.
 */
public class Container {

    private Cache cache;
    public static final String SCENE_MANAGER_EXCEPTION = "FieldManagerService does not exist";
    public static final String FIELD_MANAGER_EXCEPTION = "FieldManagerService does not exist";
    public static final String BASE_DATA_EXCEPTION = "BaseDataService does not exist";
    public static final String HUMAN_PLAYER_EXCEPTION = "HumanPlayerService does not exist";
    public static final String COMPUTER_PLAYER_EXCEPTION = "ComputerPlayerService does not exist";
    private static final String HUMAN_PLAYER_SERVICE = "HumanPlayerService";
    private static final String COMPUTER_PLAYER_SERVICE = "ComputerPlayerService";
    private static final String SCENE_MANAGER_SERVICE = "SceneManagerService";
    private static final String FIELD_MANAGER_SERVICE = "FieldManagerService";
    private static final String BASE_DATA_SERVICE = "BaseDataService";


    public Container() {
        this.cache = new Cache();
    }

    public void initializeContainer() {
        this.getService(BaseDataService.class);
        this.getService(FieldManagerService.class);
        this.getService(ComputerPlayerService.class);
        this.getService(HumanPlayerService.class);
    }

    public <T> Optional<T> getService(Class<T> serviceName) {
        BaseDataService baseDataService;
        FieldManagerService fieldManagerService;

        Optional<Service> serviceCachedOptional = this.cache.getService(serviceName.getSimpleName());

        if (serviceCachedOptional.isPresent()) {
            return (Optional<T>) serviceCachedOptional;
        }

        switch (serviceName.getSimpleName()) {
            case HUMAN_PLAYER_SERVICE:
                fieldManagerService = this.getService(FieldManagerService.class)
                        .orElseThrow(() -> new NullPointerException(FIELD_MANAGER_EXCEPTION));
                return (Optional<T>) this.cache.addService(new HumanPlayer(fieldManagerService));

            case COMPUTER_PLAYER_SERVICE:
                baseDataService = this.getService(BaseDataService.class)
                        .orElseThrow(() -> new NullPointerException(BASE_DATA_EXCEPTION));
                fieldManagerService = this.getService(FieldManagerService.class)
                        .orElseThrow(() -> new NullPointerException(FIELD_MANAGER_EXCEPTION));
                return (Optional<T>) this.cache.addService(new ComputerPlayer(fieldManagerService, baseDataService));

            case SCENE_MANAGER_SERVICE:
                baseDataService = this.getService(BaseDataService.class)
                        .orElseThrow(() -> new NullPointerException(BASE_DATA_EXCEPTION));
                fieldManagerService = this.getService(FieldManagerService.class)
                        .orElseThrow(() -> new NullPointerException(FIELD_MANAGER_EXCEPTION));
                HumanPlayerService humanPlayerService = this.getService(HumanPlayerService.class)
                        .orElseThrow(() -> new NullPointerException(HUMAN_PLAYER_EXCEPTION));
                ComputerPlayerService computerPlayerService = this.getService(ComputerPlayerService.class)
                        .orElseThrow(() -> new NullPointerException(COMPUTER_PLAYER_EXCEPTION));
                return (Optional<T>) this.cache.addService(new SceneManager(humanPlayerService, computerPlayerService, fieldManagerService, baseDataService));

            case FIELD_MANAGER_SERVICE:
                baseDataService = this.getService(BaseDataService.class)
                        .orElseThrow(() -> new NullPointerException(BASE_DATA_EXCEPTION));
                return (Optional<T>) this.cache.addService(new FieldManager(baseDataService));

            case BASE_DATA_SERVICE:
                return (Optional<T>) this.cache.addService(new BaseData());

            default:
                throw new IllegalStateException("Unexpected Service Name: " + serviceName.getSimpleName());
        }
    }

    public void addService(Service service) {
        Optional<Service> serviceCachedOptional = this.cache.getService(service.getName());
        serviceCachedOptional.ifPresent(cachedService -> this.cache.addService(service));
    }
}
