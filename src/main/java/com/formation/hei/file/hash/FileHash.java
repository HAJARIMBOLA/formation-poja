package com.formation.hei.file.hash;

import com.formation.hei.PojaGenerated;

@PojaGenerated
public record FileHash(FileHashAlgorithm algorithm, String value) {}
