"""
提示词注册表 — 从 .txt 文件加载，支持变量替换

用法:
    from app.core.prompts import get_prompt
    prompt = get_prompt("triage", symptoms="头痛发烧", knowledge="...", current_date="2026-05-30")
"""

import os
from datetime import date
from typing import Dict, Optional

_PROMPT_DIR = os.path.dirname(os.path.abspath(__file__))
_cache: Dict[str, str] = {}


def _load(name: str) -> str:
    if name not in _cache:
        path = os.path.join(_PROMPT_DIR, f"{name}.txt")
        if not os.path.exists(path):
            raise FileNotFoundError(f"提示词文件不存在: {path}")
        with open(path, "r", encoding="utf-8") as f:
            _cache[name] = f.read()
    return _cache[name]


def render(name: str, **kwargs) -> str:
    """
    加载并渲染提示词模板

    Args:
        name: 提示词名称（不含 .txt 后缀），如 "intent", "triage", "register", "report", "medication"
        **kwargs: 模板变量，如 symptoms="头痛", knowledge="...", current_date="2026-05-30"

    Returns:
        渲染后的完整提示词
    """
    template = _load(name)

    defaults = {"current_date": date.today().isoformat()}
    for key, val in defaults.items():
        if key not in kwargs:
            kwargs[key] = val

    return template.format(**kwargs)


def reload_cache():
    """清除缓存，下次调用时重新从文件加载"""
    _cache.clear()
