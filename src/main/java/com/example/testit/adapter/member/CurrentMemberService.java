package com.example.testit.adapter.member;

import java.util.Optional;

public interface CurrentMemberService {

    /**
     * Retourne l'id du membre connecté
     * @return l'id du membre, empty si personne n'est connecté
     */
    Optional<Long> getCurrentMemberId();
}
