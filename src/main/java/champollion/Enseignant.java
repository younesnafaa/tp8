package champollion;

import java.util.*;

public class Enseignant extends Personne {
    private final Map<UE, ServicePrevu> servicesPrevus = new HashMap<>();
    private final List<Intervention> interventions = new ArrayList<>();

    public Enseignant(String nom, String email) {
        super(nom, email);
    }

    public int heuresPrevues() {
        int total = 0;
        for (ServicePrevu service : servicesPrevus.values()) {
            total += service.getVolumeCM() * 1.5;
            total += service.getVolumeTD();
            total += service.getVolumeTP() * 0.75;
        }
        return (int) Math.round(total);
    }

    public int heuresPrevuesPourUE(UE ue) {
        if (!servicesPrevus.containsKey(ue)) {
            return 0;
        }
        ServicePrevu service = servicesPrevus.get(ue);
        double total = service.getVolumeCM() * 1.5 + service.getVolumeTD() + service.getVolumeTP() * 0.75;
        return (int) Math.round(total);
    }

    public void ajouteEnseignement(UE ue, int volumeCM, int volumeTD, int volumeTP) {
        ServicePrevu service = servicesPrevus.getOrDefault(ue, new ServicePrevu(ue));
        service.ajouterVolume(volumeCM, volumeTD, volumeTP);
        servicesPrevus.put(ue, service);
    }

    public void ajouteIntervention(Intervention intervention) {
        UE ue = intervention.getUe();
        TypeIntervention type = intervention.getType();
        int heuresPlanifiees = intervention.getDuree();

        if (resteAPlanifier(ue, type) < heuresPlanifiees) {
            throw new IllegalArgumentException("Heures planifiées dépassent les heures prévues.");
        }
        interventions.add(intervention);
    }

    public int resteAPlanifier(UE ue, TypeIntervention type) {
        int prevues = 0;
        if (servicesPrevus.containsKey(ue)) {
            ServicePrevu service = servicesPrevus.get(ue);
            if (type == TypeIntervention.CM) prevues = service.getVolumeCM();
            if (type == TypeIntervention.TD) prevues = service.getVolumeTD();
            if (type == TypeIntervention.TP) prevues = service.getVolumeTP();
        }

        int planifiees = interventions.stream()
                .filter(intervention -> intervention.getUe().equals(ue) && intervention.getType() == type)
                .mapToInt(Intervention::getDuree)
                .sum();

        return Math.max(0, prevues - planifiees);
    }

    public boolean enSousService() {
        return heuresPrevues() < 192;
    }
}
