package iGuard.Server.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import iGuard.Server.Dto.user.ShelterDistanceDto;
import iGuard.Server.Dto.user.ShelterSearchDto;
import iGuard.Server.Entity.Shelter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

import static iGuard.Server.Entity.QShelter.shelter;

@Repository
@RequiredArgsConstructor
public class ShelterRepositoryCustomImpl implements ShelterRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ShelterDistanceDto> search(ShelterSearchDto dto, Pageable pageable) {

        // 필터 및 정렬 적용
        BooleanBuilder builder = applyFilters(dto);
        OrderSpecifier<?> orderSpecifier = applySorting(dto);

        JPQLQuery<ShelterDistanceDto> query = queryFactory
                .select(Projections.constructor(ShelterDistanceDto.class, shelter, calculateDistance(dto)))
                .from(shelter)
                .where(builder)
                .orderBy(orderSpecifier);

        // 페이징 적용
        List<ShelterDistanceDto> content = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 전체 개수 조회
        Long total = queryFactory
                .select(shelter.count())
                .from(shelter)
                .where(builder)
                .fetchOne();

        total = (total != null) ? total : 0;

        return new PageImpl<>(content, pageable, total);
    }

    private BooleanBuilder applyFilters(ShelterSearchDto dto) {
        BooleanBuilder builder = new BooleanBuilder();

        if(StringUtils.hasText(dto.getCity())) {
            builder.and(shelter.address.like(dto.getCity()+"%"));
        }
        if(StringUtils.hasText(dto.getGu())) {
            builder.and(shelter.address.like(dto.getCity()+"%"+dto.getGu()+"%"));
        }
        if (StringUtils.hasText(dto.getShelterName())) {
            builder.and(shelter.shelterName.contains(dto.getShelterName()));
        }
        if (StringUtils.hasText(dto.getFacilityType())) {
            builder.and(shelter.facilityTypeName.eq(dto.getFacilityType()));
        }

        BooleanBuilder orBuilder = new BooleanBuilder();
        if (dto.getIsOpenAtNight() != null) {
            orBuilder.or(shelter.isOpenAtNight.eq(dto.getIsOpenAtNight()));
        }
        if (dto.getIsOpenOnHolidays() != null) {
            orBuilder.or(shelter.isOpenOnHolidays.eq(dto.getIsOpenOnHolidays()));
        }
        if (dto.getAllowsAccommodation() != null) {
            orBuilder.or(shelter.allowsAccommodation.eq(dto.getAllowsAccommodation()));
        }

        if (orBuilder.hasValue()) {
            builder.and(orBuilder);
        }

        return builder;
    }

    private OrderSpecifier<?> applySorting(ShelterSearchDto dto) {
        return switch (dto.getSortBy() != null ? dto.getSortBy() : "distance") {
            case "name" -> shelter.shelterName.asc();
            case "currentUsage" -> shelter.currentOccupancy.asc();
            case "distance" -> calculateDistance(dto).asc();
            default -> shelter.shelterName.asc();
        };
    }

    // 사용자의 현재 위치와 쉼터의 위치를 비교하여 거리 계산 (단위 : m)
    private NumberExpression<Float> calculateDistance(ShelterSearchDto dto) {
        return Expressions.numberTemplate(
                Float.class,
                "function('ST_Distance_Sphere', POINT({0}, {1}), POINT({2}, {3}))",
                dto.getLongitude(), dto.getLatitude(),
                shelter.longitude, shelter.latitude
        );
    }


}
