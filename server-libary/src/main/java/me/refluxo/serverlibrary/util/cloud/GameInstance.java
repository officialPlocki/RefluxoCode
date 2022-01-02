package me.refluxo.serverlibrary.util.cloud;

import de.dytanic.cloudnet.common.document.gson.JsonDocProperty;
import de.dytanic.cloudnet.common.document.gson.JsonDocument;
import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.driver.service.ServiceConfiguration;
import de.dytanic.cloudnet.driver.service.ServiceEnvironmentType;
import de.dytanic.cloudnet.driver.service.ServiceInfoSnapshot;
import de.dytanic.cloudnet.driver.service.ServiceTemplate;

public class GameInstance {

    private final String ident;
    private final String uuid;
    private ServiceInfoSnapshot snapshot;
    private final int memory;
    private String uid;

    public GameInstance(String ident, String uuid, int memory) {
        this.ident = ident;
        this.uuid = uuid;
        this.memory = memory;
    }

    public void addOwnerUUID(String uuid) {
        uid = uuid;
    }

    @SuppressWarnings("unused")
    public void startInstance() {
        ServiceTemplate defaultTemplate = new ServiceTemplate(ident, "default", "local");
        ServiceTemplate saveTemplate = new ServiceTemplate(ident, uuid, "saves");
        if(!defaultTemplate.storage().exists()) {
            defaultTemplate.storage().create();
        }
        if(!saveTemplate.storage().exists()) {
            defaultTemplate.storage().create();
        }
        snapshot = ServiceConfiguration.builder()
                .task(ident+uuid)
                .node(CloudNetDriver.getInstance().getNodeUniqueId())
                .autoDeleteOnStop(false)
                .staticService(true)
                .templates(defaultTemplate, saveTemplate)
                .maxHeapMemory(memory)
                .environment(ServiceEnvironmentType.MINECRAFT_SERVER)
                .build()
                .createNewService();
        if (snapshot != null) {
            snapshot.provider().start();
        }
        if(uid != null) {
            assert snapshot != null;
            snapshot.getConfiguration().getProperties().append(JsonDocument.newDocument("ownerUUID", uid));
        }
    }

    public ServiceInfoSnapshot getInstance() {
        if(CloudNetDriver.getInstance().getCloudServiceProvider().getCloudServiceByName(ident+uuid) != null) {
            return CloudNetDriver.getInstance().getCloudServiceProvider().getCloudServiceByName(ident+uuid);
        }
        return null;
    }

    @SuppressWarnings("unused")
    public void stopInstance() {
        CloudNetDriver.getInstance().getCloudServiceProvider(getInstance().getServiceId().getUniqueId()).stop();
        snapshot = null;
    }

}
