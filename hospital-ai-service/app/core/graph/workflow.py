from langgraph.graph import StateGraph, END
from app.core.graph.nodes import create_diagnosis_graph, GraphState
from app.utils.logger import log
from typing import Dict, Any


class DiagnosisWorkflow:
    """诊断工作流管理器"""

    def __init__(self):
        """初始化工作流"""
        self.graph = None
        self._compile_graph()

    def _compile_graph(self):
        """编译工作流图"""
        try:
            workflow = create_diagnosis_graph()
            self.graph = workflow.compile()
            log.info("诊断工作流编译成功")
        except Exception as e:
            log.error(f"工作流编译失败: {str(e)}")
            raise

    async def run(self, initial_state: Dict[str, Any]) -> Dict[str, Any]:
        """
        运行工作流

        Args:
            initial_state: 初始状态

        Returns:
            最终状态
        """
        try:
            log.info(f"开始运行工作流 - session_id: {initial_state.get('session_id')}")

            # 运行工作流
            final_state = await self.graph.ainvoke(initial_state)

            log.info(f"工作流执行完成 - session_id: {initial_state.get('session_id')}")

            return final_state

        except Exception as e:
            log.error(f"工作流执行失败: {str(e)}")
            raise

    async def run_stream(self, initial_state: Dict[str, Any]):
        """
        流式运行工作流

        Args:
            initial_state: 初始状态

        Yields:
            中间状态
        """
        try:
            log.info(f"开始流式运行工作流 - session_id: {initial_state.get('session_id')}")

            # 流式运行工作流
            async for state in self.graph.astream(initial_state):
                yield state

            log.info(f"流式工作流执行完成 - session_id: {initial_state.get('session_id')}")

        except Exception as e:
            log.error(f"流式工作流执行失败: {str(e)}")
            raise

    def get_graph_info(self) -> Dict[str, Any]:
        """获取工作流信息"""
        if not self.graph:
            return {}

        return {
            "nodes": list(self.graph.nodes),
            "edges": self.graph.edges,
            "entry_point": self.graph.entry_point
        }


# 全局工作流实例
workflow_instance = None


def get_workflow() -> DiagnosisWorkflow:
    """获取工作流实例"""
    global workflow_instance

    if workflow_instance is None:
        workflow_instance = DiagnosisWorkflow()

    return workflow_instance
