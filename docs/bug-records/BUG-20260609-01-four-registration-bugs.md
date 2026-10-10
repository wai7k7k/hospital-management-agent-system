---
name: bug-20260609-four-registration-bugs
description: 挂号模块4个联动Bug修复 —— 日期搜索/科室空白/人工挂号/就诊页身份证查询
metadata:
  type: reference
---

# BUG-20260609-01: 挂号模块 4 个联动 Bug

| 字段 | 内容 |
|------|------|
| **编号** | BUG-20260609-01 |
| **发现日期** | 2026-06-09 |
| **修复日期** | 2026-06-09 |
| **严重程度** | 🔴严重 |
| **影响模块** | ylxm-reghos, hospital-ai-service |
| **修复人** | xinyaohe |

---

## 一、问题描述

1. 挂号列表选日期 2026-06-09 搜索，抛出 `Failed to convert String to java.util.Date`
2. AI 挂号记录的科室名称列空白
3. 人工护士挂号后挂号列表看不到新记录 + 排班名额不扣减
4. 就诊页面用患者身份证检索不到 AI 创建的挂号单

## 二、根因分析

| Bug | 根因 | 技术细节 |
|-----|------|----------|
| 日期转换 | `RegisterBo.registerDate` 缺少 `@DateTimeFormat`，Spring 无法把 `yyyy-MM-dd` 字符串解析为 `java.util.Date` | Spring 默认日期格式依赖 Locale，中国默认 `yyyy/MM/dd` |
| 科室空白 | `queryPageList` 用 `sys_dept` 表的 ID→Name 映射覆盖了 DB 中已有的 `dep_name` 值 | `dep` 表和 `sys_dept` 表 ID 不自增同步，`map.get(1L)` 返回 null |
| 人工挂号双故障 | `GuahaoServiceImpl.guahao()` 缺少 `@Transactional` 事务注解，insert + Redis 操作不原子 | 事务回滚时 DB 和 Redis 状态不一致 |
| 就诊页查不到 | AI 挂号只写 DB 不写 Redis；`getRegisterByCardId` 只读 Redis 不读 DB | 人工挂号路径写 Redis，AI 挂号路径漏了 |

## 三、修复方案

| 文件 | 改动 |
|------|------|
| `RegisterBo.java` | 新增 `@DateTimeFormat(pattern = "yyyy-MM-dd")` |
| `RegisterServiceImpl.java` | depName 覆盖逻辑改为保护已有值 + null 兜底"无科室"；`insertByBo` 补写 Redis；新增 `queryByCardId` DB 兜底查询 |
| `GuahaoServiceImpl.java` | 新增 `@Transactional(rollbackFor = Exception.class)` |
| `RegisterController.java` | `getRegisterByCardId` Redis 未命中时调用 `queryByCardId` 从 DB 查 |
| `IRegisterService.java` | 新增 `queryByCardId` 接口方法 |

## 四、验证方法

- [x] 日期筛选 2026-06-09 不报错
- [ ] AI 挂号记录科室名正常显示
- [ ] 人工挂号提交后挂号列表可见，排班名额-1
- [ ] 就诊页输入患者身份证可检索到 AI 挂号

## 五、学习收获

### 技术知识点

* **Spring 日期绑定双通道**：`@DateTimeFormat` 作用于 Query String/表单，`@JsonFormat` 作用于 JSON body，两者独立不可互换
* **MyBatis-Plus VO 自动映射**：`selectVoPage()` 不经过 XML resultMap，直接做字段名→列名映射，DB 值可以直接透传到 VO
* **跨表 ID→Name 映射的陷阱**：两张业务表的 ID 序列独立时，不能假设 ID 一一对应
* **AI 挂号 vs 人工挂号的路径差异**：两条路径写入的数据结构相同，但写入后的副作用（Redis 缓存、事件发布）不一致，需要对齐

### 经验教训

* 排查列表字段空白时，先确认 DB 里有值（排除写入问题），再追溯查询链路（排除覆盖/映射问题）
* 人工和自动化流程要检查副作用对称性——不只是 DB 写入，还有缓存、事件、日志

### 关联知识

* [[project-knowledge/modules/ylxm-reghos]]
* [[project-knowledge/modules/hospital-ai-service]]
* [[bug-records/BUG-20260608-01-ai-404-duplicate-msg]]
