package iGuard.Server.Service.user;

import iGuard.Server.Dto.user.VisitedShelterResponse;
import iGuard.Server.Entity.Shelter;
import iGuard.Server.Entity.User;
import iGuard.Server.Entity.VisitedShelter;
import iGuard.Server.Repository.ShelterRepository;
import iGuard.Server.Repository.UserRepository;
import iGuard.Server.Repository.VisitedShelterRepository;
import iGuard.Server.Util.SecurityUserContextProvider;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class VisitedShelterServiceImpl implements VisitedShelterService {

    private final VisitedShelterRepository visitedShelterRepository;
    private final SecurityUserContextProvider userContextProvider;
    private final ShelterRepository shelterRepository;
    private final UserRepository userRepository;

    @Override
    public void createVisitedShelter(Integer userId, Integer shelterId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Shelter shelter = shelterRepository.findById(shelterId)
                .orElseThrow(() -> new RuntimeException("Shelter not found"));

        VisitedShelter visitedShelter = new VisitedShelter();
        visitedShelter.setShelter(shelter);
        visitedShelter.setUser(user);

        visitedShelterRepository.save(visitedShelter);
    }

    @Override
    public List<VisitedShelterResponse> getVisitedShelter() {
        String id = userContextProvider.getLoginUserId();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return visitedShelterRepository
                .findAllByUser_id(id)
                .stream()
                .map( shelter -> new VisitedShelterResponse(
                        shelter.getShelter(),
                        shelter.getDate().format(formatter))
                )
                .collect(Collectors.toList());
    }

}